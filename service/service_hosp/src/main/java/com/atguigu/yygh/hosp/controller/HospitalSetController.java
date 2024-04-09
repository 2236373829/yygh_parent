package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalSetQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @author xyzZero3
 * @create 2024-03-23 15:09
 */
@CrossOrigin // 解决跨域最简单的方法
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {

    // 注入service
    @Autowired
    private HospitalSetService service;

    /**
     * 查询医院设置表所有信息
     *
     * @return
     */
    @GetMapping("/findAll")
    public Result findAllHospitalSet() {
        List<HospitalSet> list = service.list();
        return Result.ok(list);
    }

    /**
     * 删除医院设置
     *
     * @param id 医院设置id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result removeHospSet(@PathVariable Long id) {
        boolean removeResult = service.removeById(id);
        return removeResult ? Result.ok(true) : Result.fail(false);
    }

    /**
     * 条件查询带分页
     *
     * @param current            当前页
     * @param limit              每页记录数
     * @param hospitalSetQueryVo 前端传递查询参数
     * @return
     */
    @PostMapping("/findPage/{current}/{limit}")
    public Result findPage(@PathVariable Long current,
                           @PathVariable Long limit,
                           @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        // 创建page对象，传递当前页，每页记录数
        Page<HospitalSet> page = new Page<>(current, limit);

        // TODO 代码应该放在service类中
        // 构造条件
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        String hosName = hospitalSetQueryVo.getHosName();
        String hosCode = hospitalSetQueryVo.getHosCode();
        if (!StringUtils.isEmpty(hosName)) {
            wrapper.like("hos_name", hospitalSetQueryVo.getHosName());
        }
        if (!StringUtils.isEmpty(hosCode)) {
            wrapper.eq("hos_code", hospitalSetQueryVo.getHosCode());
        }
        // TODO

        // 调用方法，实现分页查询
        Page<HospitalSet> hospitalSetPage = service.page(page, wrapper);
        return Result.ok(hospitalSetPage);
    }

    /**
     * 添加医院设置
     */
    @PostMapping("/saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        // 设置医院签名密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));

        boolean save = service.save(hospitalSet);
        return save ? Result.ok(true) : Result.fail(false);
    }

    /**
     * 根据id获取医院设置
     *
     * @param id 医院设置id
     * @return
     */
    @GetMapping("/getHospitalSet/{id}")
    public Result getHospitalSet(@PathVariable Long id) {
        /*try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw new YyghException("被除数不能为0", 201);
        }*/
        HospitalSet hospitalSet = service.getById(id);
        return Result.ok(hospitalSet);
    }

    /**
     * 根据id修改医院设置
     *
     * @param hospitalSet 要修改的医院设置对象
     * @return
     */
    @PutMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean update = service.updateById(hospitalSet);
        return update ? Result.ok(true) : Result.fail(false);
    }

    /**
     * 根据id批量删除医院设置
     *
     * @param idList
     * @return
     */
    @DeleteMapping("/batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        boolean batchRemove = service.removeByIds(idList);
        return batchRemove ? Result.ok(true) : Result.fail(false);
    }

    /**
     * 医院设置锁定和解锁
     *
     * @param id     医院设置id
     * @param status 医院状态
     * @return
     */
    @PutMapping("/lockHospitalSet/{id}") // /lockHospitalSet/{id}/{status}
    public Result lockHospitalSet(@PathVariable Long id) { // @PathVariable Integer status
        // 根据id查询医院设置信息
        HospitalSet hospitalSet = service.getById(id);

        // 设置状态
        if (hospitalSet.getStatus() == 1) {
            hospitalSet.setStatus(0);
        } else {
            hospitalSet.setStatus(1);
        }
        // hospitalSet.setStatus(status);
        boolean lock = service.updateById(hospitalSet);
        return lock ? Result.ok(true) : Result.fail(false);
    }

    @GetMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable Long id) {
        HospitalSet hospitalSet = service.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hosCode = hospitalSet.getHoscode();
        //TODO 发送短信

        return Result.ok();
    }

}
