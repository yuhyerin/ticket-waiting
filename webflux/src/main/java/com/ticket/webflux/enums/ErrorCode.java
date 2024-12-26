package com.ticket.webflux.enums;

import com.ticket.webflux.exception.ApplicationException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode {
    QUEUE_ALREADY_REGISTERED_USER(HttpStatus.CONFLICT, "UQ-001", "Already registered in queue");
    private final HttpStatus httpStatus;
    private final String code;
    private final String reason;

    public ApplicationException build(){
        return new ApplicationException(httpStatus, code, reason);
    }
    public ApplicationException build(Object ...args){
        return new ApplicationException(httpStatus, code, reason.formatted(args));
    }
}
