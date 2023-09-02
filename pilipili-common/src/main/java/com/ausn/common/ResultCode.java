package com.ausn.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResultCode
{
    DEFAULT_OK(10001,"DEFAULT_OK",HttpStatus.OK),

    DEFAULT_ERR(10000,"DEFAULT_ERR",HttpStatus.BAD_REQUEST),

    SAVE_OK(20011,"SAVE_OK",HttpStatus.OK),

    DELETE_OK(20021,"DELETE_OK",HttpStatus.OK),

    UPDATE_OK(20031,"UPDATE_OK",HttpStatus.OK),

    GET_OK(20041,"GET_OK",HttpStatus.OK),

    SAVE_ERR(20010,"SAVE_ERR",HttpStatus.BAD_REQUEST),

    DELETE_ERR(20020,"DELETE_ERR",HttpStatus.BAD_REQUEST),

    UPDATE_ERR(20030,"UPDATE_ERR",HttpStatus.BAD_REQUEST),

    GET_ERR(20040,"GET_ERR",HttpStatus.BAD_REQUEST),

    SYSTEM_ERR(50001,"SYSTEM_ERR",HttpStatus.BAD_REQUEST),

    SYSTEM_TIMEOUT_ERR(50002,"SYSTEM_TIMEOUT_ERR",HttpStatus.BAD_REQUEST),

    SYSTEM_UNKNOWN_ERR(59999,"SYSTEM_UNKNOWN_ERR",HttpStatus.BAD_REQUEST),

    BUSINESS_ERR(60001,"BUSINESS_ERR",HttpStatus.BAD_REQUEST),

    UNAUTHORIZED(70001,"unauthorized",HttpStatus.UNAUTHORIZED),

    PHONE_NUMBER_NOT_EXIST(70002,"phone number not exist",HttpStatus.NOT_EXTENDED);


    private int code;
    private String message;
    private HttpStatus httpStatus;

    ResultCode(int code, String message,HttpStatus httpStatus)
    {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

}