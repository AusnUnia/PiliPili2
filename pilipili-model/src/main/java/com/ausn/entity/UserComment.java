package com.ausn.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

/*
create table comments
(
 commentId long primary key,
 bv varchar(11),
 userId varchar(11),
 sendDate datetime,
 content varchar(1024),
 upvoteNum int,
 downvoteNum int,
 replyNum int
)
*/

@Data
@TableName("user_comments")
public class UserComment
{
    @TableId
    private Long id;
    private Long commentId; //评论编号
    private String bv; //所属视频编号
    private Long userId;  //发布评论的用户的id
    private Timestamp sendDate; //评论发布日期
    private String content; //评论内容
    private int upvoteNum; //点赞数量
    private int downvoteNum; //点踩数量
    private int replyNum; //该评论下回复的数量
}
