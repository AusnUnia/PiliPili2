package com.ausn.common.exception.bizException;

import com.ausn.common.ResultCode;
import com.ausn.common.exception.ApiException;
import com.ausn.common.exception.BizException;

/**
 * @Author: 张坤
 * @DateTime: 2023/7/10 17:22
 * @Description: 该类用于 未授权异常
 */
public class UnAuthorizedException extends BizException
{
    public UnAuthorizedException()
    {
        super("invalid token", ResultCode.UNAUTHORIZED);
    }
    public UnAuthorizedException(String message)
    {
        super(message,ResultCode.UNAUTHORIZED);
    }
}
