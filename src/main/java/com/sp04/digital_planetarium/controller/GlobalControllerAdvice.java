package com.sp04.digital_planetarium.controller;

import com.sp04.digital_planetarium.entity.Response;
import com.sp04.digital_planetarium.exception.BadRequestException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Response constraintViolationExceptionHandler(ConstraintViolationException e) {
        return new Response(400, e.getMessage());
    }

    @ExceptionHandler(value = BadRequestException.class)
    public Response badRequestExceptionHandler(BadRequestException e) {
        Response response = new Response(e.getCode(), e.getMessage());
        response.setData(e.getData());
        return response;
    }

    @ExceptionHandler(value = Exception.class)
    public Response exceptionHandler(Exception e) {
        return new Response(500, e.getMessage());
    }
}
