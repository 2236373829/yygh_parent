package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-09 9:26
 */
public interface ScheduleService {
    /**
     * 上传排班接口
     * @param paramMap
     */
    void saveSchedule(Map<String, Object> paramMap);

    /**
     * 查询排班接口
     * @param page
     * @param limit
     * @param scheduleQueryVo
     * @return
     */
    Page<Schedule> findPageSchedule(Integer page, Integer limit, ScheduleQueryVo scheduleQueryVo);

    void removeSchedule(String hoscode, String hosScheduleId);

    /**
     * 根据医院编号和科室编号查询排班规则
     *
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     */
    Map<String, Object> getScheduleRule(Integer page, Integer limit, String hoscode, String depcode);

    /**
     * 根据医院编号、科室编号和工作日期查询排班详细信息
     *
     * @param hoscode
     * @param depcode
     * @param workDate
     * @return
     */
    List<Schedule> getScheduleDetail(String hoscode, String depcode, String workDate);
}
