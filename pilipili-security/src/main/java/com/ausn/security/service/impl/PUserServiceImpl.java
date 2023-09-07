package com.ausn.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.ausn.common.Result;
import com.ausn.common.ResultCode;
import com.ausn.common.constants.LocalConstants;
import com.ausn.common.constants.RedisConstants;
import com.ausn.common.utils.PUserUtil;
import com.ausn.entity.PUser;
import com.ausn.entity.dto.LoginFormDTO;
import com.ausn.entity.dto.PUserDTO;
import com.ausn.security.dao.PUserDao;
import com.ausn.security.service.PUserService;
import com.ausn.security.strategy.login.LoginStrategy;
import com.ausn.security.strategy.login.LoginStrategyFactory;
import com.ausn.common.utils.JwtUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PUserServiceImpl extends ServiceImpl<PUserDao, PUser> implements PUserService
{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PUserDao pUserDao;
    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    @Override
    @Transactional
    public PUser createUserWithPhoneNumberAndPassword(String phoneNumber, String password)
    {
        PUser pUser=new PUser();
        pUser.setPhoneNumber(phoneNumber);
        pUser.setNickName(LocalConstants.PUSER_NICK_NAME_PREFIX+RandomUtil.randomNumbers(11));
        pUser.setAvatarPath("./images/avatars/default.jpg");
        pUser.setGender("unknown");
        pUser.setBirthday(Date.valueOf(LocalDate.now()));

        String salt = RandomUtil.randomString(16);
        pUser.setSalt(salt);

        String encodedPassword= DigestUtils.sha256Hex(password+salt);
        pUser.setPassword(encodedPassword);
        save(pUser);
        pUser.setUid(pUserDao.getLastInsertedId());

        return pUser;
    }

    @Override
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


    @Override
    public Result login(LoginFormDTO loginFormDTO)
    {
        log.info(loginFormDTO.toString());

        //login by type
        LoginStrategy strategy = loginStrategyFactory.getStrategy(loginFormDTO.getType());

        PUser pUser = strategy.login(loginFormDTO);

        //cache the information of the user in Redis
        String token= saveUserInRedis(pUser);

        //return the token to the front end
        return Result.ok(ResultCode.DEFAULT_OK,token);
    }


    public String saveUserInRedis(PUser pUser)
    {
        //cache the information of the user in Redis

        //generate token, this is the key to identify whether a user has logged in.
        String token= JwtUtils.encodeToken(pUser);

        //cache user's information in Redis in the form of hash
        PUserDTO pUserDTO= BeanUtil.copyProperties(pUser, PUserDTO.class); //extract the insensitive information of the user
        pUserDTO.setToken(token);
        Map<String,Object> pUserMap=BeanUtil.beanToMap(
                pUserDTO,new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((field,val)->val.toString())
        );                                                                 //convert the bean of user's insensitive information into map which can be accepted by the Redis
        //System.out.println("in PUSerServ: pUserMap:"+pUserMap);
        stringRedisTemplate.opsForHash().putAll(RedisConstants.LOGIN_PUSER_KEY_PREFIX+pUser.getUid(),pUserMap);

        //set the login period of validity
        stringRedisTemplate.expire(RedisConstants.LOGIN_PUSER_KEY_PREFIX+token,RedisConstants.LOGIN_PUSER_TTL, TimeUnit.MINUTES);

        return token;
    }


    @Override
    public Result sendVerificationCode(String phoneNumber)
    {
        //check the phpne number
        if(!PUserUtil.isPhoneNumberValid(phoneNumber))
        {
            return Result.fail(ResultCode.BUSINESS_ERR,"请输入正确的手机号");
        }

        //generate the verification code
        String verificationCode= RandomUtil.randomNumbers(6);

        //save the verification code in redis, later the login business will check it.
        //the verification code is accessible in 3 minutes.
        stringRedisTemplate.opsForValue()
                .set(RedisConstants.LOGIN_CODE_KEY_PREFIX+phoneNumber,verificationCode,
                        RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);

        //send the verification code (it's just simulate the process)
        log.debug("发送短信验证码成功，验证码：{}",verificationCode);
        System.out.println("发送短信验证码成功，验证码："+verificationCode);

        return Result.ok();
    }
}
