package com.ausn.video.dao;

import com.ausn.entity.VideoVote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoVoteDao
{
    public int save(VideoVote videoVote);
    public int delete(@Param("bv") String bv, @Param("userId")Long userId);
    public VideoVote getByBvUserId(@Param("bv") String bv, @Param("userId")Long userId);
    public int update(VideoVote videoVote);
    public List<VideoVote> getByBv(@Param("bv") String bv);
}
