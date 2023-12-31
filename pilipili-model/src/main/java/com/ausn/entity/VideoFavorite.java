package com.ausn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/*
the relationship between user saved or not and video
 */
@Data
@TableName("user_favorites")
public class VideoFavorite
{
    private String bv;
    private Long userId;
    private LocalDateTime favoriteTime;
}

/*
create table video_saves
(
    bv varchar(11),
    userId bigint,
    saveTime timestamp,
    primary key(bv,userId)
)
 */
