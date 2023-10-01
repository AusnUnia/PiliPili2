package com.ausn.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class BulletScreen
{
    private String bv; //所属视频编号
    private int sendTimeSec; //弹幕发送时间（在该视频第几秒发送的）
    private LocalDateTime sendTime; //发送时间
    private String content; //弹幕内容
}