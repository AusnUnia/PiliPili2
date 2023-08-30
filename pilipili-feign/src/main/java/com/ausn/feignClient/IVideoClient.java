package com.ausn.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/30 16:12
 * @Description:
 */
@FeignClient("pilipili-video")
@RequestMapping("/videos")
public interface IVideoClient
{
    @PostMapping("/updateCommentNumByBv/{bv}/{num}")
    int updateCommentNumByBv(@PathVariable("bv") String bv,@PathVariable("num") int num);
}
