package com.ausn.user;

import com.ausn.feign.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients(defaultConfiguration={FeignConfig.class})
@EnableDiscoveryClient
@EnableTransactionManagement
@ComponentScan({"com.ausn.common.config","com.ausn.user"})
public class UserApplication
{
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
