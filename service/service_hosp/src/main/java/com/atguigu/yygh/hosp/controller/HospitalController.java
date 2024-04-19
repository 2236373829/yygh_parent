package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-09 20:44
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 医院列表（条件分页查询）
     * @param page
     * @param limit
     * @param hospitalQueryVo
     * @return
     */
    @GetMapping("/list/{page}/{limit}")
    public Result hospList(@PathVariable Integer page,
                           @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitalPage = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitalPage);
    }

    /**
     * 更新医院上线状态
     *
     * @return
     */
    @GetMapping("/updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable String id,
                               @PathVariable Integer status) {
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    /**
     * 医院详情信息
     *
     * @param id
     * @return
     */
    @GetMapping("/showHospDetail/{id}")
    public Result showHospDetail(@PathVariable String id) {
        Map<String, Object> hospitalMap = hospitalService.getHospById(id);
        return Result.ok(hospitalMap);
    }

}
