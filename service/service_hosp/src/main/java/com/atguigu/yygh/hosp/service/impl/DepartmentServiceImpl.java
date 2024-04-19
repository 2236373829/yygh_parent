package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xyzZero3
 * @create 2024-04-08 18:55
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        // paramMap转换成department对象
        String departmentString = JSONObject.toJSONString(paramMap);
        Department department = JSONObject.parseObject(departmentString, Department.class);

        // 根据医院编号和科室编号进行查询
        Department departmentExist = departmentRepository.
                getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (departmentExist != null) {
            departmentExist.setUpdateTime(new Date());
            departmentExist.setIsDeleted(0);
            departmentRepository.save(departmentExist);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        // 创建Pageable对象
        Pageable pageable = PageRequest.of((page - 1), limit);

        // 创建Example对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Department> example = Example.of(department, matcher);

        Page<Department> departmentPage = departmentRepository.findAll(example, pageable);
        return departmentPage;
    }

    @Override
    public void removeDepartment(String hoscode, String depcode) {
        // 根据医院编号和科室编号查询科室信息
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            // 调用方法删除
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> findDepTree(String hoscode) {
        // 创建list集合，用于最终的数据封装
        List<DepartmentVo> result = new ArrayList<>();

        // 根据医院编号，查询医院所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example<Department> example = Example.of(departmentQuery);
        // 所有科室信息
        List<Department> allDepartment = departmentRepository.findAll(example);
        // 根据大科室编号 bigcode 分组，获取每个大科室里面下级子科室
        Map<String, List<Department>> departmentMap = allDepartment.stream()
                .collect(Collectors.groupingBy(Department::getBigcode));

        // 遍历map集合departmentMap
        departmentMap.forEach((bigCode, bigDepartmentList) -> {
            // 封装大科室
            DepartmentVo bigDepartmentVo = new DepartmentVo();
            bigDepartmentVo.setDepcode(bigCode);
            bigDepartmentVo.setDepname(bigDepartmentList.get(0).getDepname());

            // 封装小科室集合
            List<DepartmentVo> childrenDepartmentList = new ArrayList<>();
            bigDepartmentList.forEach(department -> {
                DepartmentVo childrenDepartment = new DepartmentVo();
                childrenDepartment.setDepcode(department.getDepcode());
                childrenDepartment.setDepname(department.getDepname());
                childrenDepartmentList.add(childrenDepartment);
            });

            bigDepartmentVo.setChildren(childrenDepartmentList);
            result.add(bigDepartmentVo);
        });


        /*for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            // 大科室编号
            String bigcode = entry.getKey();
            // 大科室编号对应的科室信息
            List<Department> bigDepartmentList = entry.getValue();

            // 封装大科室
            DepartmentVo bigDepartmentVo = new DepartmentVo();
            bigDepartmentVo.setDepcode(bigcode);
            bigDepartmentVo.setDepname(bigDepartmentList.get(0).getDepname());

            // 封装小科室
            List<DepartmentVo> childrenDepartmentList = new ArrayList<>();
            bigDepartmentList.forEach(item -> {
                DepartmentVo childrenDepartment = new DepartmentVo();
                childrenDepartment.setDepcode(item.getDepcode());
                childrenDepartment.setDepname(item.getDepname());
                childrenDepartmentList.add(childrenDepartment);
            });
            bigDepartmentVo.setChildren(childrenDepartmentList);
            result.add(bigDepartmentVo);
        }*/

        return result;
    }

    @Override
    public String getDepName(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            return department.getDepname();
        }
        return null;
    }
}
