package com.ausn.feign.interceptior;

import com.alibaba.fastjson2.JSON;
import com.ausn.common.utils.UserHolder;
import com.ausn.entity.dto.PUserDTO;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;



@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate)
    {
        PUserDTO user = UserHolder.getUser();
        if (user != null) {
            try
            {
                String userJson = JSON.toJSONString(user);
                requestTemplate.header("KEY_USERINFO_IN_HTTP_HEADER",new String[]{URLDecoder.decode(userJson,"UTF-8")});
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}