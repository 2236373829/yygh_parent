package com.atguigu.yygh.msm.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.msm.service.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xyzZero3
 * @create 2024-04-25 17:36
 */
@RestController
@RequestMapping("/api/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{email}")
    public Result send(@PathVariable String email) {
        boolean isSend = msmService.send(email);
        return isSend ? Result.ok() : Result.fail().message("发送短信失败");
    }

}
