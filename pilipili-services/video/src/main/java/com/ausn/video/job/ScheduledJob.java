package com.ausn.video.job;

import com.ausn.common.constants.RedisConstants;
import com.ausn.entity.VideoVote;
import com.ausn.video.dao.VideoDao;
import com.ausn.video.dao.VideoVoteDao;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/14 21:01
 * @Description:
 */

@Component
public class ScheduledJob
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private VideoVoteDao videoVoteDao;
    static private final ExecutorService SCHEDULED_TASK_EXECUTOR;

    static
    {
        SCHEDULED_TASK_EXECUTOR=new ThreadPoolExecutor(
                4,4,
                0, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(16),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @XxlJob("expireKeyAtMidnight")// 每天零点触发任务
    public void expireKeyAtMidnight()
    {
        int shardTotal = XxlJobHelper.getShardTotal();
        int shardIndex = XxlJobHelper.getShardIndex();

        Set<String> keys = stringRedisTemplate.keys( RedisConstants.VIDEO_COIN_TODAY_CACHE_KEY_PREFIX+ "*");
        if(keys!=null&&!keys.isEmpty())
        {
            for(String key:keys)
            {
                if(key.hashCode()%shardTotal==shardIndex)
                {
                    stringRedisTemplate.delete(key);
                }
            }
        }
    }


    @XxlJob("persistData") //每180秒执行一次
    @Transactional
    public void persistData()
    {
        persistVote(RedisConstants.VIDEO_VOTE_CACHE_KEY_PREFIX);
    }

    @Transactional
    public void persistVote(String prefix)
    {
        int shardTotal = XxlJobHelper.getShardTotal();
        int shardIndex = XxlJobHelper.getShardIndex();

        Set<String> keys = stringRedisTemplate.keys(prefix + "*");
        if(keys!=null&&!keys.isEmpty())
        {
            for(String key:keys)
            {
                if(key.hashCode()%shardTotal==shardIndex)
                {
                    SCHEDULED_TASK_EXECUTOR.submit( ()->{updateVoteInMysql(prefix,key);} );
                }
            }
        }
    }

    @Transactional
    public void updateVoteInMysql(String prefix,String key)
    {
        String bv=key.substring(prefix.length());
        Map<Long, Integer> userIdVotes =stringRedisTemplate.opsForHash().entries(key).entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> Long.parseLong(entry.getKey().toString()),
                        entry -> Integer.parseInt(entry.getValue().toString())
                ));

        //update the vote relationship in table video_votes
        int upvoteNum=0;
        int downvoteNum=0;
        for(Map.Entry<Long, Integer> entry: userIdVotes.entrySet())
        {
            Long userId=entry.getKey();
            int vote=entry.getValue();

            if(vote==1)
            {
                upvoteNum++;
            }
            if(vote==-1)
            {
                downvoteNum++;
            }

            VideoVote videoVote = videoVoteDao.getByBvUserId(bv, userId);
            if(videoVote==null)
            {
                videoVote=new VideoVote();
                videoVote.setBv(bv);
                videoVote.setUserId(userId);
            }

            videoVote.setVote(vote);

            if(videoVoteDao.update(videoVote)==0)
            {
                throw new RuntimeException("Failed to update vote relationship in table video_votes");
            }
        }

        videoDao.updateUpvoteNumByBv(bv,upvoteNum);
        videoDao.updateDownvoteNumByBv(bv,downvoteNum);
    }

}
