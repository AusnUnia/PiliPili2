package com.ausn.user.controller;


import com.ausn.common.Result;
import com.ausn.common.ResultCode;
import com.ausn.common.utils.UserHolder;
import com.ausn.entity.VideoFeedStream;
import com.ausn.entity.requestEntity.NewVideosRequest;
import com.ausn.user.service.PUserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class PUserController
{
    @Autowired
    private PUserService pUserService;

    /*
    if a user refresh the updates list of users he followed, this method will be called.
     */

    @GetMapping("/pullNewVideos")
    public Result pull(@RequestBody NewVideosRequest newVideosRequest)
    {
        Long userId= UserHolder.getUser().getUid();

        boolean pulled=pUserService.refreshVideoList(userId, newVideosRequest.getLastBv(),newVideosRequest.getTime());

        if(!pulled)
        {
            return Result.fail("刷新失败！");
        }

        List<VideoFeedStream> newVideoList = pUserService.getNewVideoList(userId, newVideosRequest.getLastBv(), newVideosRequest.getTime());
        return Result.ok(ResultCode.DEFAULT_OK,newVideoList);
    }

}
