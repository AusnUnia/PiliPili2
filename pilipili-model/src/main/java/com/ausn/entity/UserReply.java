package com.ausn.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/*
create table comments
(
 commentId int,
 userId varchar(11),
 replyUserId varchar(11),
 sendDate datetime,
 content varchar(512),
 upvoteNum int,
 downvoteNum int,
)
*/

@Data
public class UserReply
{
    private Long commentId; //所属评论编号
    private Long userId;  //发布的用户的id
    private Long replyUserId; //所回复的那个用户的id
    private LocalDateTime sendTime; //回复发布时间
    private String content; //回复内容
    private int upvoteNum; //点赞数量
    private int downvoteNum; //点踩数量
}
