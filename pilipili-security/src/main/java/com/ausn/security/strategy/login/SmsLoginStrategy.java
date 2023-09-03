package com.ausn.security.strategy.login;

import cn.hutool.core.util.RandomUtil;
import com.ausn.common.constants.LocalConstants;
import com.ausn.common.constants.RedisConstants;
import com.ausn.common.utils.PUserUtil;
import com.ausn.entity.PUser;
import com.ausn.entity.dto.LoginFormDTO;
import com.ausn.security.dao.PUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/27 20:52
 * @Description:
 */
@Component
public class SmsLoginStrategy implements LoginStrategy
{
    @Autowired
    private PUserDao pUserDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public PUser login(LoginFormDTO loginFormDTO)
    {
        System.out.println(loginFormDTO);
        //check the phone number
        String phoneNumber= loginFormDTO.getPhoneNumber();
        if(!PUserUtil.isPhoneNumberValid(phoneNumber))
        {
            throw new RuntimeException("请输入正确手机号！");
        }

        //confirm the verification code
        boolean isValid=confirmVerificationCode(phoneNumber,loginFormDTO.getVerificationCode());
        if(!isValid)
        {
            throw new RuntimeException("验证码错误！");
        }

        //query the user by the phone number
        PUser pUser= pUserDao.getByPhoneNumber(phoneNumber);
        if(pUser==null)
        {
            //if didn't find the user, it's new user, create it and save in database
            pUser=createUserWithPhoneNumber(phoneNumber);
        }

        return pUser;
    }

    public boolean confirmVerificationCode(String phoneNumber, String verificationCode)
    {
        String cachedCode=stringRedisTemplate.opsForValue()
                .get(RedisConstants.LOGIN_CODE_KEY_PREFIX+phoneNumber);

        //if the cached verification code is not consistent with the verification code
        if(cachedCode==null||!cachedCode.equals(verificationCode))
        {
            System.out.println("cached:"+cachedCode);
            System.out.println("verificationCode:"+verificationCode);
            return false;
        }

        return true;
    }

    @Transactional
    public PUser createUserWithPhoneNumber(String phoneNumber)
    {
        PUser pUser=new PUser();
        pUser.setPhoneNumber(phoneNumber);
        pUser.setNickName(LocalConstants.PUSER_NICK_NAME_PREFIX+RandomUtil.randomNumbers(11));
        pUser.setAvatarPath("./images/avatars/default.jpg");
        pUser.setGender("unknown");
        pUser.setBirthday(Date.valueOf(LocalDate.now()));
        pUserDao.save(pUser);
        pUser.setUid(pUserDao.getLastInsertedId());

        return pUser;
    }
}
