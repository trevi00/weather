package com.zerobase.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500번대 에러 서버에 문제가 있음
    @ExceptionHandler(Exception.class)
    public Exception handleAllException(){
        System.out.println("error from GlobalExceptionHandler");
        return new Exception("error from GlobalExceptionHandler");
    }
}
