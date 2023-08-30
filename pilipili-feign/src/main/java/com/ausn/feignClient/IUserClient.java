package com.ausn.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/29 20:37
 * @Description:
 */

@FeignClient("user-service")
public interface IUserClient
{

}
