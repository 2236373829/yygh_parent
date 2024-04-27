package com.atguigu.yygh.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author xyzZero3
 * @create 2024-04-22 13:06
 */
@Configuration
@MapperScan("com.atguigu.yygh.user.mapper")
public class UserConfig {
}
