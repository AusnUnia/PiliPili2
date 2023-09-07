package com.ausn.comment.kafka;

import com.ausn.entity.dto.PUserDTO;
import lombok.Data;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/8 2:14
 * @Description:
 */
@Data
public class KafkaMessage<T>
{
    private PUserDTO pUserDTO;
    private T data;
}
