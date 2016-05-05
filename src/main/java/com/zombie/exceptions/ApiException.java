package com.zombie.exceptions;

/**
 *
 */

public class ApiException extends Exception {
    public ApiException(String errorCode, String msg) {
        super("error code:" + errorCode + "\nerror message:" + msg);
    }
}
