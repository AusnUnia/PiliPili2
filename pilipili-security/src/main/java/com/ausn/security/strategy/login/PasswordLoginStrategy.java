package com.ausn.security.strategy.login;

import com.ausn.common.exception.bizException.UnAuthorizedException;
import com.ausn.common.utils.PUserUtil;
import com.ausn.entity.PUser;
import com.ausn.entity.dto.LoginFormDTO;
import com.ausn.security.dao.PUserDao;
import com.ausn.security.entity.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;


/**
 * @Author: 付显贵
 * @DateTime: 2023/8/27 20:52
 * @Description:
 */
@Component
@Slf4j
public class PasswordLoginStrategy implements LoginStrategy
{
    @Autowired
    PUserDao pUserDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public PUser login(LoginFormDTO loginFormDTO)
    {
        //check the phone number
        String phoneNumber= loginFormDTO.getPhoneNumber();
        if(!PUserUtil.isPhoneNumberValid(phoneNumber))
        {
            throw new RuntimeException("请输入正确手机号！");
        }

        /*
        use authentication manager to check the password.
        The manage will use UserDetailsService::loadUserByUsername to get the user information
        and
         */

        UsernamePasswordAuthenticationToken authentication=
                new UsernamePasswordAuthenticationToken(phoneNumber,loginFormDTO.getPassword());
        Authentication auth=null;
        try
        {
            auth = authenticationManager.authenticate(authentication);
        }
        catch(AuthenticationException e)
        {
            System.out.println("Authentication failed:" + e.getMessage());
            e.printStackTrace();
        }

        if(auth==null||!auth.isAuthenticated())
        {
            log.info("密码错误！");
            throw new UnAuthorizedException("密码错误！");
        }

        /*
        if successfully authenticated, the principal will become an SecurityUser
        which is the return value of UserDetailsService::loadUserByUsername
         */
        SecurityUser principal = (SecurityUser)auth.getPrincipal();

        return principal.getPuser();
    }
}
