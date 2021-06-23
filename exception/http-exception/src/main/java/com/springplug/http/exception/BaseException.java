package com.springplug.http.exception;


public abstract class BaseException extends RuntimeException {
    protected Object data;

    public BaseException() {
    }

    public BaseException(Object data, String message) {
        super(message);
        this.data = data;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * 异常对应的的状态码
     *
     * @return 状态码枚举类
     */
    public abstract int getHttpStatus();

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public Object getData() {
        return data;
    }
}
