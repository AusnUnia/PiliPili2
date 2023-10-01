package com.ausn.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/*
the relationship between user's downvote or upvote and video
 */

/*
create table video_votes
(
 bv varchar(11),
 userId Bigint,
 downvote boolean,
 upvote boolean,
 primary key (bv,userId)
)
*/
@Data
@TableName("user_votes")
public class VideoVote
{
    @TableId
    private Long id;
    private String bv;
    private Long userId;
    private int vote; // 1 is upvote, 0 is no action, -1 is downvote
}
