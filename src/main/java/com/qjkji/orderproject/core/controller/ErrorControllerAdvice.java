package com.qjkji.orderproject.core.controller;

import com.qjkji.orderproject.core.entity.Result;
import com.qjkji.orderproject.core.exception.UnmessageException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(UnmessageException.class)
    public Result errorHandler(UnmessageException e){
        return Result.error(e.getMessage());
    }

}
