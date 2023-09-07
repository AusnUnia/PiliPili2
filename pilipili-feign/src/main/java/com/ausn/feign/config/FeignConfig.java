package com.ausn.feign.config;

import com.ausn.feign.interceptor.FeignInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/7 2:09
 * @Description:
 */

@Configuration
public class FeignConfig
{
    @Bean
    public FeignInterceptor feginInterceptor()
    {
        return new FeignInterceptor();
    }
}
