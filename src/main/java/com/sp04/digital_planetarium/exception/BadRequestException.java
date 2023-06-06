package com.sp04.digital_planetarium.exception;

public class BadRequestException extends Exception{

    private String message;
    private Object data;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getCode(){
        return 400;
    }

    public BadRequestException(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
