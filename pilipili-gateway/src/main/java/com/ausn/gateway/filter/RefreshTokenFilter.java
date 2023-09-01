package com.ausn.gateway.filter;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.ausn.common.constants.RedisConstants;
import com.ausn.common.utils.UserHolder;
import com.ausn.entity.dto.PUserDTO;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Order(1)
@Component
public class RefreshTokenFilter implements GlobalFilter
{
    private StringRedisTemplate stringRedisTemplate; //this is not managed by spring, must inject it through constructor

    public RefreshTokenFilter(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate=stringRedisTemplate;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        //when finish request , always clear localthread
        Mono<Void> ret = chain.filter(exchange).then(Mono.fromRunnable(UserHolder::removeUser));

        //get token
        System.out.println("RefreshToeknInterceptor1:"+exchange.getRequest().getURI());
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(StrUtil.isBlank(token))
        {
            System.out.println("blank token! "+exchange.getRequest().getURI());
            return ret;
        }
        System.out.println("RefreshToeknInterceptor2:"+exchange.getRequest().getURI());
        //determine whether there is the user or not
        String key= RedisConstants.LOGIN_PUSER_KEY_PREFIX+token;
        Map<Object,Object> userMap=stringRedisTemplate.opsForHash().entries(key);
        if(userMap.isEmpty())
        {
            //if the user don't exist ,just let it pass, the LoginIntercepter will handle
            System.out.println("redis中用户不存在! key:"+key);
            return ret;
        }
        System.out.println("RefreshToeknInterceptor3:"+exchange.getRequest().getURI());
        //save the user information in ThreadLocal. every time the user send a request the user information will be saved in TheadLocal and will be deleted after the request completed (in afterCompletion method)
        //System.out.println("in refresh interceptor: userMap:"+userMap);
        PUserDTO pUserDTO= BeanUtil.fillBeanWithMap(userMap,new PUserDTO(),false);
        UserHolder.saveUser(pUserDTO);
        System.out.println("RefreshToeknInterceptor4:"+exchange.getRequest().getURI());
        //refresh the token, if the user are frequently using , this will be triggered again and again, the user won't be logged out due to timeout.
        stringRedisTemplate.expire(key,RedisConstants.LOGIN_PUSER_TTL, TimeUnit.MINUTES);
        System.out.println("RefreshToeknInterceptor5:"+exchange.getRequest().getURI());
        return ret;
    }
}
