package com.atguigu.yygh.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;

/**
 * @author xyzZero3
 * @create 2024-04-23 18:01
 */
@SpringBootTest
class Mail {

    @Autowired
    private JavaMailSender javaMailSender;


    @Test
    void test() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("验证码");
        message.setFrom("2236373829@qq.com");
        message.setTo("2403620498@qq.com");
        message.setSentDate(new Date());
        message.setText("验证码：301475");
        javaMailSender.send(message);
    }
}
