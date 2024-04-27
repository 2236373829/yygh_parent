package com.atguigu.yygh.user.controller;

import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-22 13:03
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    // 用户手机号登录
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        Map<String, Object> loginInfo = userInfoService.userLogin(loginVo);
        return Result.ok(loginInfo);
    }

}
