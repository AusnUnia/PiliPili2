package com.ausn.entity.requestEntity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/24 19:23
 * @Description:
 */
@Data
public class NewVideosRequest
{
    String lastBv;
    LocalDateTime time;
}
