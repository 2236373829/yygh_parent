package com.atguigu.yygh.cmn.controller;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author xyzZero3
 * @create 2024-04-01 12:25
 */
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    private DictService dictService;

    @Autowired
    public void setDictService(DictService dictService) {
        this.dictService = dictService;
    }

    /**
     * 导出数据字典
     * @return
     */
    @GetMapping("/exportDict")
    public void exportDict(HttpServletResponse response) {
        dictService.exportDict(response);
    }

    @PostMapping("/importDict")
    public void importDict(MultipartFile file) {
        dictService.importDict(file);
    }

    /**
     * 查询id的子节点
     * @param id
     * @return
     */
    @GetMapping("/findChildrenData/{id}")
    public Result findChildrenData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildDate(id);
        return Result.ok(list);
    }

    /**
     * 根据dictCode和value值查询
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/getDictName/{dictCode}/{value}")
    public String getDictName(@PathVariable String dictCode,
                              @PathVariable String value) {
        return dictService.getDictName(dictCode, value);
    }

    @GetMapping("/getDictName/{value}")
    public String getDictName(@PathVariable String value) {
        return dictService.getDictName("", value);
    }

    /**
     * 根据dictCode获取下级节点
     *
     * @param dictCode
     * @return
     */
    @GetMapping("/findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> dictList = dictService.findByDictCode(dictCode);
        return Result.ok(dictList);
    }

}
