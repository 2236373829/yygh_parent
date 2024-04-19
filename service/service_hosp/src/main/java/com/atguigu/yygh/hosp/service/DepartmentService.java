package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-08 18:54
 */
public interface DepartmentService {

    // 上传科室接口
    void save(Map<String, Object> paramMap);

    // 分页查询科室信息接口
    Page<Department> findPageDepartment(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo);

    //删除科室接口
    void removeDepartment(String hoscode, String depcode);

    // 根据医院编号查询科室信息
    List<DepartmentVo> findDepTree(String hoscode);

    // 根据医院编号和科室编号查询科室名称
    String getDepName(String hoscode, String depcode);
}
