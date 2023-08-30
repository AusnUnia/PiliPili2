package com.ausn.gateway.config;

import com.ausn.gateway.filter.LoginFilter;
import com.ausn.gateway.filter.RefreshTokenFilter;
import com.ausn.gateway.filter.ReqLimitFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig
{
    @Bean
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder,
            RefreshTokenFilter filter1,
            LoginFilter filter2,
            ReqLimitFilter filter3
    )
    {

        //TODO 写过滤链
        return null;
    }

}