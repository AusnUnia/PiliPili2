package com.ausn.security.utils;

import com.ausn.common.exception.bizException.UnAuthorizedException;
import com.ausn.entity.PUser;
import com.ausn.security.entity.SecurityUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserHolder {
    /**
     * 获取用户信息
     * 注意会抛出异常
     *
     */
    public static PUser getUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated())
        {
            if(authentication.getPrincipal() instanceof String)
            {
                throw new UnAuthorizedException(authentication.getPrincipal().toString());
            }
            SecurityUser principal = (SecurityUser)authentication.getPrincipal();
            // 其他处理逻辑
            return principal.getPuser();
        }
        else
        {
            throw new UnAuthorizedException();
        }
    }
}