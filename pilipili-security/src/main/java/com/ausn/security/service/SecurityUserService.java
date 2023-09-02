package com.ausn.security.service;

import cn.hutool.core.util.StrUtil;
import com.ausn.common.security.dao.PUserDao;
import com.ausn.common.utils.PUserUtil;
import com.ausn.entity.PUser;
import com.ausn.security.entity.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author: 付显贵
 * @DateTime: 2023/9/2 19:23
 * @Description:
 */

@Service
public class SecurityUserService implements UserDetailsService
{
    @Autowired
    private PUserDao pUserDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        String phoneNumber=username;
        if(PUserUtil.isPhoneNumberValid(phoneNumber))
        {
            throw new UsernameNotFoundException("请输入正确手机号！");
        }

        PUser pUser = pUserDao.getByPhoneNumber(phoneNumber);
        if(pUser==null)
        {
            throw new UsernameNotFoundException("该用户不存在！");
        }

        SecurityUser securityUser=new SecurityUser(pUser);
        return securityUser;
    }
}
