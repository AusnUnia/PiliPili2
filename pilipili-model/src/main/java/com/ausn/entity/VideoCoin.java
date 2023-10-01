package com.ausn.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;

/*
the relationship between user saved or not and video
 */
@Data
@TableName("user_coins")
public class VideoCoin
{
    private Long id;
    private String bv; //the id of video that the user put coin
    private Long userId; //the user's uid
    private Integer coinNum; //the number of coins put one time
    private LocalDate putDate; //the time of putting coins, a user can only put at most 2 coins to one video each day.
}

/*
 create table video_coins
 (
    bv varchar(11),
    userId bigint,
    coinNum short,
    putDate date,
    primary key(bv,userId,putTime)
 )
 */