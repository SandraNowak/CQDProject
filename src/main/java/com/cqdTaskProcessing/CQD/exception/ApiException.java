package com.cqdTaskProcessing.CQD.exception;

public class ApiException extends RuntimeException{

    private final ErrorCode code;
    private final String message;

    public ApiException(String message, ErrorCode code) {
        this.code = code;
        this.message = message;
    }
}

