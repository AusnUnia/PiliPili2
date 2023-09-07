package com.ausn.feign.feignClient;

import com.ausn.feign.config.FeignConfig;
import com.ausn.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/29 20:37
 * @Description:
 */

@FeignClient(value="user-service",configuration = FeignInterceptor.class)
public interface IUserClient
{

}
