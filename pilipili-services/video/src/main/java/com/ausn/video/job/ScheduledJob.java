package com.ausn.video.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * @Author: Ausn
 * @DateTime: 2023/9/14 21:01
 * @Description:
 */

@Component
public class ScheduledJob
{
    @XxlJob("demo")
    public void demo() throws Exception
    {
        System.out.println("demo---------------------demo");
    }
}
