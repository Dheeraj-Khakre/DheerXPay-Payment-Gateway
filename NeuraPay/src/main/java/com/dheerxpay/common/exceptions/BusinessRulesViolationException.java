package com.dheerxpay.common.exceptions;

import lombok.Getter;

@Getter
public class BusinessRulesViolationException extends RuntimeException{
    private String errorCode;
    public BusinessRulesViolationException(String errorCode, String message) {

        super(message);
        this.errorCode = errorCode;
    }
}
