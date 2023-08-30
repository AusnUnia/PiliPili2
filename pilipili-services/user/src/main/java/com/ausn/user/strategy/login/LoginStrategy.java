package com.ausn.user.strategy.login;


import com.ausn.entity.PUser;
import com.ausn.entity.dto.LoginFormDTO;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/27 20:53
 * @Description:
 */
public interface LoginStrategy
{
    PUser login(LoginFormDTO loginFormDTO);
}
