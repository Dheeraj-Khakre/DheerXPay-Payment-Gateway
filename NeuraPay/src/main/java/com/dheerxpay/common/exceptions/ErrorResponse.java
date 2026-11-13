package com.dheerxpay.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String errorCore,
        String errorDescription,
        Instant timestamp,
        List<FieldError> fieldErrors

) {
     public record FieldError(String field,String message){}
    public static ErrorResponse of(String errorCore,String errorDescription){
         return new ErrorResponse(errorCore,errorDescription,Instant.now(),null);
    }
    public static ErrorResponse of(String errorCore,String errorDescription, List<FieldError> fieldError){
        return new ErrorResponse(errorCore,errorDescription,Instant.now(),fieldError);
    }
}
