package com.ausn.user.service.impl;

import cn.hutool.crypto.symmetric.ZUC;
import com.ausn.common.constants.RedisConstants;
import com.ausn.entity.PUser;
import com.ausn.entity.VideoFavorite;
import com.ausn.entity.VideoFeedStream;
import com.ausn.feign.feignClient.IVideoClient;
import com.ausn.user.dao.PUserDao;
import com.ausn.user.service.PUserService;
import com.ausn.user.service.UserFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PUserServiceImpl extends ServiceImpl<PUserDao, PUser> implements PUserService
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PUserDao pUserDao;

    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private IVideoClient videoClient;

    /*
    refresh current user's feed stream box
    * */

    @Override
    public boolean refreshVideoList(Long userId,String lastBv,LocalDateTime startTime)
    {
        if (lastBv == null || lastBv.isBlank()||startTime==null)
        {
            startTime = LocalDateTime.now();
        }

        //get authors followed by current user
        List<Long> authorIds = userFollowService.getAuthorIds(userId);

        List<ZSetOperations.TypedTuple<String>> videoFeedStreams=new ArrayList<>();
        double scoreOfNow=LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        double scoreOfStartTime=startTime.toEpochSecond(ZoneOffset.UTC);
        for(Long authorId:authorIds)
        {
            String publishBoxKey= RedisConstants.PUSER_PUBLISH_BOX_PREFIX+authorId;
            videoFeedStreams.addAll(
                    stringRedisTemplate
                            .opsForZSet()
                            .rangeByScoreWithScores(publishBoxKey,scoreOfStartTime,scoreOfNow)
            );
        }

        String feedBoxKey= RedisConstants.PUSER_FEED_BOX_PREFIX+userId;

        for(ZSetOperations.TypedTuple<String> tuple:videoFeedStreams)
        {
            String bv=tuple.getValue();
            double score = tuple.getScore();

            //if two video were uploaded at the same time, sort them by dic order.
            if(lastBv!=null&&!lastBv.isBlank())
            {
                if(score==(double)startTime.toEpochSecond(ZoneOffset.UTC)&&bv.compareTo(lastBv)<=0)
                {
                    continue;
                }
            }
            stringRedisTemplate.opsForZSet().add(feedBoxKey,bv,score);
        }

        return true;
    }

    @Override
    public List<VideoFeedStream> getNewVideoList(Long userId, String lastBv, LocalDateTime startTime)
    {
        String feedBoxKey= RedisConstants.PUSER_FEED_BOX_PREFIX+userId;
        double scoreOfStartTime=startTime.toEpochSecond(ZoneOffset.UTC);
        double scoreOfNow=LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

        if(lastBv==null||lastBv.isBlank())
        {
            return stringRedisTemplate
                    .opsForZSet()
                    .rangeByScoreWithScores(feedBoxKey, scoreOfStartTime, scoreOfNow)
                    .stream()
                    .map(tuple -> new VideoFeedStream(tuple.getValue(),tuple.getScore())) //convert tuple to videoFeedStream
                    .toList();
        }

        List<VideoFeedStream> videoStreamList=new ArrayList<>();

        videoStreamList.addAll(
                stringRedisTemplate
                    .opsForZSet()
                    .rangeByScoreWithScores(feedBoxKey, scoreOfStartTime, scoreOfStartTime)
                    .stream()
                    .filter(tuple -> lastBv.compareTo(tuple.getValue())<0) //get those videos that are uploaded later than lastBv
                    .map(tuple -> new VideoFeedStream(tuple.getValue(),tuple.getScore())) //convert tuple to videoFeedStream
                    .toList()
        );

        videoStreamList.addAll(
                stringRedisTemplate
                    .opsForZSet()
                    .rangeByScoreWithScores(feedBoxKey, scoreOfStartTime+1, scoreOfNow)
                    .stream()
                    .map(tuple -> new VideoFeedStream(tuple.getValue(),tuple.getScore()))
                    .toList()
        );

        return videoStreamList;
    }
}
