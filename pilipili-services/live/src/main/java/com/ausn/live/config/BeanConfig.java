package com.ausn.live.config;

import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/6 16:55
 * @Description:
 */

@Configuration
public class BeanConfig
{
    @Bean
    public KurentoClient kurentoClient()
    {
        return KurentoClient.create();
    }
}
