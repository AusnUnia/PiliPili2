package com.ausn.common.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.ausn.common.constants.RedisConstants;
import com.ausn.common.utils.UserHolder;
import com.ausn.entity.dto.PUserDTO;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
        System.out.println("RefreshToeknInterceptor1:"+request.getRequestURI());
        String token=request.getHeader("Authorization");
        if(StrUtil.isBlank(token))
        {
            System.out.println("blank token! "+request.getRequestURI());
            return true;
        }
        System.out.println("RefreshToeknInterceptor2:"+request.getRequestURI());
        //determine whether there is the user or not
        String key= RedisConstants.LOGIN_PUSER_KEY_PREFIX+token;
        Map<Object,Object> userMap=stringRedisTemplate.opsForHash().entries(key);
        if(userMap.isEmpty())
        {
            //if the user don't exist ,just let it pass, the LoginIntercepter will handle
            System.out.println("redis中用户不存在! key:"+key);
            return true;
        }
        System.out.println("RefreshToeknInterceptor3:"+request.getRequestURI());
        //save the user information in ThreadLocal. every time the user send a request the user information will be saved in TheadLocal and will be deleted after the request completed (in afterCompletion method)
        //System.out.println("in refresh interceptor: userMap:"+userMap);
        PUserDTO pUserDTO= BeanUtil.fillBeanWithMap(userMap,new PUserDTO(),false);
        UserHolder.saveUser(pUserDTO);
        System.out.println("RefreshToeknInterceptor4:"+request.getRequestURI());
        //refresh the token, if the user are frequently using , this will be triggered again and again, the user won't be logged out due to timeout.
        stringRedisTemplate.expire(key,RedisConstants.LOGIN_PUSER_TTL, TimeUnit.MINUTES);
        System.out.println("RefreshToeknInterceptor5:"+request.getRequestURI());
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
