package com.ausn.gateway.filter;


import com.ausn.common.Result;
import com.ausn.common.constants.RedisConstants;
import com.ausn.common.utils.IpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Order(3)
@Component
public class ReqLimitFilter implements GlobalFilter
{
    private StringRedisTemplate stringRedisTemplate;

    public ReqLimitFilter(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate=stringRedisTemplate;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String path = exchange.getRequest().getURI().getPath();

        //only limit requests that is related to upvote and comment publish
        if(!path.contains("upvote")&&!path.contains("/publish"))
        {
            System.out.println("pass !!");
            return chain.filter(exchange);
        }

        String ipAddr = IpUtil.getIpAddr(exchange.getRequest());
        String requestURI = path;
        String key= RedisConstants.REQUEST_LIMIT_KEY_PREFIX+requestURI+":"+ipAddr;
        System.out.println("ReqLimit:"+key);

        String countStr = stringRedisTemplate.opsForValue().get(key);
        if(countStr==null)
        {
            stringRedisTemplate.opsForValue().set(key,"1");
            stringRedisTemplate.expire(key,1, TimeUnit.MINUTES);
            return chain.filter(exchange);
        }

        Integer count=Integer.parseInt(countStr);
        //reject the frequent request
        if(count>=5)
        {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            try
            {
                byte[] responseBytes = new ObjectMapper().writeValueAsBytes(Result.fail("please try later..."));
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(responseBytes);
                return exchange.getResponse().writeWith(Mono.just(buffer));

            }
            catch (JsonProcessingException e)
            {
                throw new RuntimeException(e);
            }

        }

        stringRedisTemplate.opsForValue().increment(key);

        return chain.filter(exchange);
    }

}
