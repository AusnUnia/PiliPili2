package com.ausn.security.service;

import com.ausn.common.Result;
import com.ausn.entity.PUser;
import com.ausn.entity.dto.LoginFormDTO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PUserService extends IService<PUser>
{
    Result login(LoginFormDTO loginFormDTO);

    Result sendVerificationCode(String phoneNumber);

    boolean confirmVerificationCode(String phoneNumber, String verificationCode);

    PUser createUserWithPhoneNumberAndPassword(String phoneNumber, String password);
}
