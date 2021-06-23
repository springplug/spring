package com.springplug.web.mvc.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jwt
 * @version 1.0
 * @date 2021/4/22 15:31
 */
@Slf4j
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    protected int code;

    protected final String message;

    protected Object data;

    public BaseException(String message)
    {
        this.code = 500;
        this.message = message;
    }

    public BaseException(String message,Object data)
    {
        this.code = 500;
        this.message = message;
        this.data=data;
    }

    public BaseException(String message, Throwable e)
    {
        super(message, e);
        this.code = 500;
        this.message = message;
    }

    public BaseException(String message, Object data,Throwable e)
    {
        super(message, e);
        this.code = 500;
        this.data = data;
        this.message = message;
    }

    public BaseException(int code, String message){
        this.code = code;
        this.message = message;
    }

    public BaseException(int code, String message, Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseException(int code, String message, Throwable e){
        super(message, e);
        this.code = code;
        this.message = message;
    }

    public BaseException(int code, String message, Object data, Throwable e){
        super(message, e);
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage()
    {
        return message;
    }
}
