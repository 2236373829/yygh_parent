package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-07 22:07
 */
public interface HospitalService {
    /**
     * 上传医院接口
     *
     * @param paramMap
     */
    void save(Map<String, Object> paramMap);

    /**
     * 根据医院编号查询
     *
     * @param hoscode
     * @return
     */
    Hospital getByHoscode(String hoscode);

    /**
     * 医院列表 条件分页查询
     *
     * @param page
     * @param limit
     * @param hospitalQueryVo
     * @return
     */
    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    /**
     * 更新医院上线状态
     *
     * @param id
     * @param status
     */
    void updateStatus(String id, Integer status);

    /**
     * 医院详情信息
     *
     * @param id
     * @return
     */
    Map<String, Object> getHospById(String id);

    /**
     * 根据医院编号获取医院名称
     * @param hoscode
     * @return
     */
    String getHosName(String hoscode);

    /**
     * 根据医院名称查询
     *
     * @param hosname
     * @return
     */
    List<Hospital> findByHosname(String hosname);

    /**
     * 根据医院编号获取医院详情信息
     *
     * @param hoscode
     * @return
     */
    Map<String, Object> getHospDetail(String hoscode);
}
