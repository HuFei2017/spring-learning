package com.learning.exception;

public class MyException extends RuntimeException {

    public MyException(String msg) {
        super(msg);
    }

    public MyException(Throwable cause) {
        super(cause);
    }

    public MyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
