package com.ausn.video.feignClient;

import com.ausn.entity.VideoFeedStream;
import com.ausn.feign.feignClient.IVideoClient;
import com.ausn.video.dao.VideoDao;
import com.ausn.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/30 16:44
 * @Description:
 */

@RestController
@RequestMapping("/videos")
public class VideoClientImpl implements IVideoClient
{
    @Autowired
    private VideoDao videoDao;

    @Override
    @PostMapping("/updateCommentNumByBv/{bv}/{num}")
    public int updateCommentNumByBv(
            @PathVariable("bv") String bv,
            @PathVariable("num") int num
    )
    {
        return videoDao.updateCommentNumByBv(bv,num);
    }

    @Override
    public List<VideoFeedStream> getNewVideos(Long authorId, LocalDateTime time)
    {
        return videoDao.getNewVideos(authorId,time);
    }
}
