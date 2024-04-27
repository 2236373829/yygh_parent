package com.atguigu.yygh.user.service;

import com.atguigu.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-22 13:03
 */
public interface UserInfoService {
    /**
     * 用户登录
     *
     * @param loginVo
     * @return
     */
    Map<String, Object> userLogin(LoginVo loginVo);
}
