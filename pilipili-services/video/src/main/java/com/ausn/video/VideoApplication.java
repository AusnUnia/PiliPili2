package com.ausn.video;


import com.ausn.feign.config.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients(defaultConfiguration={FeignConfig.class})
@EnableDiscoveryClient
@ComponentScan({"com.ausn.common","com.ausn.video"})
public class VideoApplication
{
    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class, args);
    }
}


