package com.atguigu.yygh.hosp.repository;

import com.atguigu.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xyzZero3
 * @create 2024-04-08 18:17
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    // 上传科室接口
    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
