package com.ausn.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableWebSecurity
@ComponentScan({"com.ausn.common.config","com.ausn.security"})
public class SecurityApplication
{
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }
}
