package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.BookingScheduleRuleVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-09 9:26
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;


    @Override
    public void saveSchedule(Map<String, Object> paramMap) {
        // paramMap转换成schedule对象
        String scheduleString = JSONObject.toJSONString(paramMap);
        Schedule schedule = JSONObject.parseObject(scheduleString, Schedule.class);

        // 根据医院编号和科室编号进行查询
        Schedule scheduleExist = scheduleRepository.
                getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (scheduleExist != null) {
            scheduleExist.setUpdateTime(new Date());
            scheduleExist.setIsDeleted(0);
            scheduleExist.setStatus(1);
            scheduleRepository.save(scheduleExist);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            scheduleRepository.save(schedule);
        }
    }

    @Override
    public Page<Schedule> findPageSchedule(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo) {
        // 创建Pageable对象
        Pageable pageable = PageRequest.of((page - 1), limit);

        // 创建Example对象
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setStatus(1);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, matcher);

        Page<Schedule> schedulePage = scheduleRepository.findAll(example, pageable);
        return schedulePage;
    }

    @Override
    public void removeSchedule(String hoscode, String hosScheduleId) {
        // 根据医院编号和排班id查询排班信息
        Schedule schedule = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, hosScheduleId);
        if (schedule != null) {
            scheduleRepository.deleteById(schedule.getId());
        }
    }

    @Override
    public Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode) {
        // 根据医院编号和科室编号查询
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);

        // 根据工作日workDate进行分组
        Aggregation aggregation = Aggregation.newAggregation(
                // 匹配条件
                Aggregation.match(criteria),
                // 分组字段
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")

                        // 统计号源数量
                        .count().as("docCount")
                        .sum("reservedNumber").as("reservedNumber")
                        .sum("availableNumber").as("availableNumber"),

                // 排序
                Aggregation.sort(Sort.Direction.DESC, "workDate"),

                // 分页
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );

        // 调用方法
        AggregationResults<BookingScheduleRuleVo> aggResult = mongoTemplate
                .aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = aggResult.getMappedResults();

        // 分组查询的总记录数
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> totalAggResult = mongoTemplate
                .aggregate(totalAgg, Schedule.class, BookingScheduleRuleVo.class);
        int total = totalAggResult.getMappedResults().size();

        // 把日期对应的星期获取
        bookingScheduleRuleVoList.forEach(item -> {
            Date workDate = item.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            item.setDayOfWeek(dayOfWeek);
        });

        // 设置最终呢数据
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList", bookingScheduleRuleVoList);
        result.put("total", total);

        // 其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosName", hospitalService.getHosName(hoscode));

        result.put("baseMap", baseMap);
        return result;
    }

    @Override
    public List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate) {
        List<Schedule> scheduleDetailList = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode,
                depcode, new DateTime(workDate).toDate());
        scheduleDetailList.forEach(this::packageSchedule);

        return scheduleDetailList;
    }

    /**
     * 封装排班详情的其他信息
     *
     * @param schedule
     */
    private void packageSchedule(Schedule schedule) {
        // 设置医院名称
        schedule.getParam().put("hosname", hospitalService.getHosName(schedule.getHoscode()));

        // 设置科室名称
        schedule.getParam().put("depcode", departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));

        // 设置日期对应星期的值
        schedule.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }

    /**
     * 根据日期获取周几数据
     *
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

}
