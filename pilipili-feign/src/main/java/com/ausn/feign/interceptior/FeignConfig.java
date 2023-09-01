package com.ausn.feign.interceptior;

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
/*        PUserDTO user = UserHolder.getUser();
        if (user != null) {
            try {
                String userJson = JSON.toJSONString(user);
                requestTemplate.header("KEY_USERINFO_IN_HTTP_HEADER",new String[]{URLDecoder.decode(userJson,"UTF-8")});
            } catch (UnsupportedEncodingException e) {
                log.error("用户信息设置错误",e);
            }
        }*/
    }
}