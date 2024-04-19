package com.atguigu.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author xyzZero3
 * @create 2024-03-23 13:01
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.atguigu")
@EnableFeignClients(basePackages = "com.atguigu")
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class, args);
    }
}
