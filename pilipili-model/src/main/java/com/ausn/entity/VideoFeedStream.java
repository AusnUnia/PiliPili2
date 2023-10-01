package com.ausn.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @Author: Ausn
 * @DateTime: 2023/10/1 18:49
 * @Description:
 */
@Data
public class VideoFeedStream
{
    private String bv;
    private LocalDateTime uploadDate; //上传日期

    public VideoFeedStream(){};

    public VideoFeedStream(String bv,Double timestamp)
    {
        this.bv=new String(bv);
        uploadDate=LocalDateTime.ofEpochSecond(Math.round(timestamp),0, ZoneOffset.UTC);
    }
}
