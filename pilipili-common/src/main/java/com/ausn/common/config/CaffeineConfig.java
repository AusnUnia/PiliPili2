package com.ausn.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/28 23:42
 * @Description:
 */

@Configuration
public class CaffeineConfig
{
    @Bean
    public CacheManager caffeineCacheManager()
    {
        SimpleCacheManager cacheManager=new SimpleCacheManager();
        List<CaffeineCache> caches=new ArrayList<>();

        caches.add(
                new CaffeineCache(
                        "test",
                        Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build()
                )
        );

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
