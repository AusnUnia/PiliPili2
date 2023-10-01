package com.ausn.feign.feignClient;


import com.ausn.entity.VideoFeedStream;
import com.ausn.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/30 16:12
 * @Description:
 */
@FeignClient(value="pilipili-video",configuration = FeignInterceptor.class)
public interface IVideoClient
{

    @PostMapping("/videos/updateCommentNumByBv/{bv}/{num}")
    int updateCommentNumByBv(@PathVariable("bv") String bv,@PathVariable("num") int num);

    @PostMapping("/video/getNewVideos")
    List<VideoFeedStream> getNewVideos(Long authorId, LocalDateTime time);
}
