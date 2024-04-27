package com.atguigu.yygh.user.service.impl;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.JwtHelper;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.user.mapper.UserInfoMapper;
import com.atguigu.yygh.user.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xyzZero3
 * @create 2024-04-22 13:03
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> userLogin(LoginVo loginVo) {
        // 从loginVo中获取输入的手机号和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();

        // 判断值是否为空
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        //todo 判断验证码是否一致
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode))
            throw new YyghException(ResultCodeEnum.CODE_ERROR);

        // 判断是否第一次登录，第一次登录就注册 返回登录信息
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        if (userInfo == null) {
            // 添加信息到数据库
            userInfo = new UserInfo();
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(1);
            int insert = baseMapper.insert(userInfo);
        }

        // 校验是否被禁用
        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        // 不是第一次直接登录 返回登录信息
        Map<String, Object> userInfoMap = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name))
            name = userInfo.getNickName();

        if (StringUtils.isEmpty(name))
            name = userInfo.getPhone();

        userInfoMap.put("name", name);

        // 使用JWT 生成token
        String token = JwtHelper.createToken(userInfo.getId(), userInfo.getName());

        userInfoMap.put("token", token);
        return userInfoMap;
    }
}
