package com.ausn.common.exception;

import com.ausn.common.ResultCode;
import lombok.Getter;


@Getter
public class ApiException extends RuntimeException
{
    private final ResultCode resultCode;

    public ApiException(String message, ResultCode resultCode) {
        super(message);
        this.resultCode = resultCode;
    }
    public ApiException(ResultCode resultCode)
    {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(String message, Throwable ex)
    {
        super(message, ex);
        this.resultCode= ResultCode.SYSTEM_UNKNOWN_ERR;
    }
}
