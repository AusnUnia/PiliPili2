package com.ausn.video;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling //定时将redis中的数据同步到mysql
@ComponentScan({"com.ausn.common","com.ausn.video"})
public class VideoApplication
{
    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
    }
}
