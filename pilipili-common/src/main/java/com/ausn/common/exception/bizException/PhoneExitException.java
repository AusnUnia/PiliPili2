package com.ausn.common.exception.bizException;


import com.ausn.common.ResultCode;
import com.ausn.common.exception.BizException;

/**
 * @Author: 张坤
 * @DateTime: 2023/7/28 19:25
 * @Description: 该类用于 电话已存在异常
 */
public class PhoneExitException extends BizException
{
    public PhoneExitException() {
        super(ResultCode.PHONE_NUMBER_NOT_EXIST);
    }
}
