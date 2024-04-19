package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xyzZero3
 * @create 2024-04-13 14:28
 */
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 根据医院编号查询科室信息
     *
     * @param hoscode
     * @return
     */
    @GetMapping("/getDepList/{hoscode}")
    public Result getDepListByHoscode(@PathVariable String hoscode) {
        List<DepartmentVo> departmentVoList = departmentService.findDepTree(hoscode);
        return Result.ok(departmentVoList);
    }

}
