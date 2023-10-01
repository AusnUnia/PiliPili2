package com.ausn.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.sql.Timestamp;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/24 19:02
 * @Description:
 */
public class UserFollow
{
    @TableId
    private Long id;
    private Long followerId;
    private Long authorId;
    private int status; //1 is followed , 0 is unfollowed
}
