package com.ausn.user.controller;


import com.ausn.common.Result;
import com.ausn.common.ResultCode;
import com.ausn.common.utils.PUserUtil;
import com.ausn.entity.PUser;
import com.ausn.entity.dto.LoginFormDTO;
import com.ausn.user.service.PUserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class PUserController
{
    @Autowired
    private PUserService pUserService;

    @PostMapping("/code")
    public Result sendVerificationCode(@RequestParam("phone") String phoneNumber)
    {
        System.out.println("sendVerificationCode");
        return pUserService.sendVerificationCode(phoneNumber);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginFormDTO)
    {
        return pUserService.login(loginFormDTO);
    }

    @PostMapping("/register")
    public Result register(@RequestBody LoginFormDTO loginFormDTO)
    {
        System.out.println(loginFormDTO);
        //check the phone number
        String phoneNumber= loginFormDTO.getPhoneNumber();
        if(!PUserUtil.isPhoneNumberValid(phoneNumber))
        {
            return Result.fail(ResultCode.BUSINESS_ERR,"请输入正确的手机号");
        }

        //confirm the verification code
        boolean isValid=pUserService.confirmVerificationCode(phoneNumber,loginFormDTO.getVerificationCode());
        if(!isValid)
        {
            return Result.fail("验证码错误");
        }


        //query the user by the phone number
        PUser pUser= pUserService.query().eq("phone_number",phoneNumber).one();
        if(pUser!=null)
        {
            return Result.fail("该手机号已经注册过了！");
        }

        pUser=pUserService.createUserWithPhoneNumberAndPassword(phoneNumber,loginFormDTO.getPassword());                                                              //convert the bean of user's insensitive information into map which can be accepted by the Redis

        if(pUser!=null)
        {
            return Result.ok(ResultCode.DEFAULT_OK,"注册成功");
        }

        return Result.fail("注册失败");
    }

}
