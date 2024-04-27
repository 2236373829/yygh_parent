package com.atguigu.yygh.msm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author xyzZero3
 * @create 2024-04-25 17:34
 */
// 取消数据源自动配置
@SpringBootApplication(scanBasePackages = "com.atguigu", exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class ServiceMsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceMsmApplication.class, args);
    }
}
