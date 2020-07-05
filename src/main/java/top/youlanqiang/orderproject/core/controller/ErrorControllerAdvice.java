package top.youlanqiang.orderproject.core.controller;

import top.youlanqiang.orderproject.core.entity.Result;
import top.youlanqiang.orderproject.core.exception.UnmessageException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(UnmessageException.class)
    public Result errorHandler(UnmessageException e){
        return Result.error(e.getMessage());
    }

}
