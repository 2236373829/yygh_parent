package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-13 18:07
 */
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 根据医院编号和科室编号查询排班规则
     *
     * @param page
     * @param limit
     * @param hoscode
     * @param depcode
     * @return
     */
    @GetMapping("/getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getScheduleRule(@PathVariable Integer page,
                                  @PathVariable Integer limit,
                                  @PathVariable String hoscode,
                                  @PathVariable String depcode) {
        Map<String, Object> scheduleRule = scheduleService.getScheduleRule(page, limit, hoscode, depcode);
        return Result.ok(scheduleRule);
    }

    /**
     * 根据医院编号、科室编号和工作日期查询排班详细信息
     *
     * @param hoscode
     * @param depcode
     * @param workDate
     * @return
     */
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public Result getScheduleDetail(@PathVariable String hoscode,
                                    @PathVariable String depcode,
                                    @PathVariable String workDate) {
         List<Schedule> scheduleDetailList = scheduleService.getScheduleDetail(hoscode, depcode, workDate);
        return Result.ok(scheduleDetailList);
    }

}
