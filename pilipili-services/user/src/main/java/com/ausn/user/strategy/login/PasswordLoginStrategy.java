package com.ausn.user.strategy.login;

;
import com.ausn.common.utils.PUserUtil;
import com.ausn.entity.PUser;
import com.ausn.entity.dto.LoginFormDTO;
import com.ausn.user.dao.PUserDao;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 付显贵
 * @DateTime: 2023/8/27 20:52
 * @Description:
 */
@Component
public class PasswordLoginStrategy implements LoginStrategy
{
    @Autowired
    PUserDao pUserDao;

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

        //query the user by the phone number
        PUser pUser= pUserDao.getByPhoneNumber(phoneNumber);
        if(pUser==null)
        {
            throw new RuntimeException("该手机号尚未注册！");
        }

        String passwordFromUser= DigestUtils.sha256Hex(loginFormDTO.getPassword()+pUser.getSalt());
        if(pUser.getPassword()==null||!pUser.getPassword().equals(passwordFromUser))
        {
            throw new RuntimeException("密码错误！");
        }

        return pUser;
    }
}
