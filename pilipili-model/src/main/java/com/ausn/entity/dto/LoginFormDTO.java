package com.ausn.entity.dto;

import lombok.Data;

@Data
public class LoginFormDTO
{
    private String phoneNumber;
    private String verificationCode;
    private String password;
    private String type;
}
