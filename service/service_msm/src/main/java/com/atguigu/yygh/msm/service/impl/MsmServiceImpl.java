package com.atguigu.yygh.msm.service.impl;

import com.atguigu.yygh.msm.service.MsmService;
import com.atguigu.yygh.msm.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author xyzZero3
 * @create 2024-04-25 17:37
 */
@Service
public class MsmServiceImpl implements MsmService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean send(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("登录注册验证码");
        message.setFrom("2236373829@qq.com");
        message.setTo(email);
        message.setSentDate(new Date());
        String code = RandomUtil.getSixBitRandom();
        message.setText("验证码：" + code);
        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            return false;
        }
        redisTemplate.opsForValue().set(email, code, 2, TimeUnit.MINUTES);

        return true;
    }
}
