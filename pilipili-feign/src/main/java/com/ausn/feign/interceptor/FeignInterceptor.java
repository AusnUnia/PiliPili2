package com.ausn.feign.interceptor;

import com.ausn.common.utils.UserHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Slf4j
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 从header获取token
        //RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(),true);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attr = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request=null;
        String token=null;
        if(attr==null)
        {
            System.out.println("attr为空！！！！");
        }
        if(attr!=null)
        {
            request = attr.getRequest();
            token = request.getHeader(HttpHeaders.AUTHORIZATION);//网关传过来的 token
        }

        if(UserHolder.getUser()!=null)
        {
            log.info("succeed in getting token");
            token=UserHolder.getUser().getToken();
        }

        if(token==null)
        {
            System.out.println("token为空！！！！!!!!!!!!!!!!!!!!!!!!!!");
            new RuntimeException("token为空！！！！").printStackTrace();
        }

        template.header(HttpHeaders.AUTHORIZATION, token);

    }

}