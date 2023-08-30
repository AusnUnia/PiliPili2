package com.ausn.video.dao;


import com.ausn.entity.VideoCoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 付显贵
 * @DateTime: 2023/7/22 15:17
 * @Description:
 */
@Mapper
public interface VideoCoinDao
{
    public int save(VideoCoin videoVote);
    public int delete(@Param("id") Long id);
    public List<VideoCoin> getByBvAndUserId(@Param("bv") String bv, @Param("userId")Long userId);
    public VideoCoin getLatestByBvAndUserId(@Param("bv") String bv, @Param("userId")Long userId);
    public List<VideoCoin> getByBv(@Param("bv") String bv);
    public int update(VideoCoin videoCoin);
}
