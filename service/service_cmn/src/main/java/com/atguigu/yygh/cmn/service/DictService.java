package com.atguigu.yygh.cmn.service;

import com.atguigu.yygh.model.cmn.Dict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xyzZero3
 * @create 2024-04-01 12:18
 */
public interface DictService extends IService<Dict> {

    // 根據id查詢子數據列表
    List<Dict> findChildDate(Long id);

    // 导出数据字典
    void exportDict(HttpServletResponse response);

    // 导入数据字典
    void importDict(MultipartFile file);
}
