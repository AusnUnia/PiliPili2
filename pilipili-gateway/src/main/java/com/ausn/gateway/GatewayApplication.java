package com.ausn.gateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/30 18:02
 * @Description:
 */

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
