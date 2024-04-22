package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.cmn.client.DictFeignClient;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-07 22:07
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;



    @Override
    public void save(Map<String, Object> paramMap) {
        // 把参数map集合转换为Hospital对象
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        // 判断是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

        if (hospitalExist != null) { // 数据存在
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else { // 数据不存在
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode);
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        // 创建pageable对象
        Pageable pageable = PageRequest.of((page - 1), limit);
        // 创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        // hospitalQueryVo转换成hospital对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        Example<Hospital> example = Example.of(hospital, matcher);

        final Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        pages.getContent().forEach(this::setHospitalType);

        return pages;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        // 根据id查询要修改的医院信息
        Hospital hospital = hospitalRepository.findById(id).get();

        // 设置修改的值
        hospital.setStatus(status);
        hospital.setUpdateTime(new Date());
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> getHospById(String id) {
        Hospital hospital = this.setHospitalType(hospitalRepository.findById(id).get());
        Map<String, Object> hospitalMap = new HashMap<>();
        hospitalMap.put("hospital", hospital);
        hospitalMap.put("bookingRule", hospital.getBookingRule());
        hospital.setBookingRule(null);
        return hospitalMap;
    }

    @Override
    public String getHosName(String hoscode) {
        return hospitalRepository.getHospitalByHoscode(hoscode).getHosname();
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> getHospDetail(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalType(this.getByHoscode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    private Hospital setHospitalType(Hospital hospital) {
        String hostype = dictFeignClient.getDictName("Hostype", hospital.getHostype());
        // 省 市 区
        String province = dictFeignClient.getDictName(hospital.getProvinceCode());
        String city = dictFeignClient.getDictName(hospital.getCityCode());
        String district = dictFeignClient.getDictName(hospital.getDistrictCode());

        hospital.getParam().put("hostypeString", hostype);
        hospital.getParam().put("fullAddress", province + city + district);
        return hospital;
    }


}
