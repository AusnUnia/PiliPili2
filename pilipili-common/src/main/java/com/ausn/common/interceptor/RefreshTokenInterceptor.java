package com.ausn.common.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.ausn.common.constants.RedisConstants;
import com.ausn.common.utils.UserHolder;
import com.ausn.entity.dto.PUserDTO;
import com.ausn.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j

public class RefreshTokenInterceptor implements HandlerInterceptor
{
    private StringRedisTemplate stringRedisTemplate; //this is not managed by spring, must inject it through constructor

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate)
    {
        this.stringRedisTemplate=stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        //get token
        String token=request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StrUtil.isBlank(token))
        {
            System.out.println("blank token! "+request.getRequestURI());
            return true;
        }

        //parse the token to get user id
        Claims claims = JwtUtils.decodeToken(token);
        if (claims == null || !claims.containsKey("uid"))
        {
            log.info("token: "+token);
            return true;
        }
        Long id =Math.round((Double)claims.get("uid"));


        //determine whether there is the user or not
        String key= RedisConstants.LOGIN_PUSER_KEY_PREFIX+id;
        Map<Object,Object> userMap=stringRedisTemplate.opsForHash().entries(key);
        if(userMap.isEmpty())
        {
            //if the user don't exist ,just let it pass, the LoginIntercepter will handle
            System.out.println("redis中用户不存在! key:"+key);
            return true;
        }

        //save the user information in ThreadLocal. every time the user send a request the user information will be saved in TheadLocal and will be deleted after the request completed (in afterCompletion method)
        //System.out.println("in refresh interceptor: userMap:"+userMap);
        PUserDTO pUserDTO= BeanUtil.fillBeanWithMap(userMap,new PUserDTO(),false);
        pUserDTO.setToken(token);
        UserHolder.saveUser(pUserDTO);

        //refresh the token, if the user are frequently using , this will be triggered again and again, the user won't be logged out due to timeout.
        stringRedisTemplate.expire(key,RedisConstants.LOGIN_PUSER_TTL, TimeUnit.MINUTES);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        //the afterCompletion method will always execute if the preHandler return true.
        System.out.println("removeUser()");
        UserHolder.removeUser();
    }
}
