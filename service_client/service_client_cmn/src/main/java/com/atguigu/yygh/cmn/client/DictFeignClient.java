package com.atguigu.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author xyzZero3
 * @create 2024-04-10 13:03
 */
@FeignClient("service-cmn")
@Component
public interface DictFeignClient {

    /**
     * 根据dictCode和value值查询
     *
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getDictName/{dictCode}/{value}")
    String getDictName(@PathVariable("dictCode") String dictCode,
                       @PathVariable("value") String value);

    /**
     * 根据value值查询
     *
     * @param value
     * @return
     */
    @GetMapping("/admin/cmn/dict/getDictName/{value}")
    String getDictName(@PathVariable("value") String value);
}
