package com.ausn.common.exception;

import com.ausn.common.ResultCode;
import lombok.Getter;


@Getter
public class BizException extends ApiException
{
    public BizException(String msg, ResultCode resultCode) {
        super(msg,resultCode);
    }
    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage(),resultCode);
    }
}
