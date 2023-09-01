package com.ausn.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan({"com.ausn.common","com.ausn.feignClient","com.ausn.comment"})
public class CommentApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(CommentApplication.class, args);
    }

}
