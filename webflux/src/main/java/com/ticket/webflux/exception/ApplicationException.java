package com.ticket.webflux.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ApplicationException extends RuntimeException {
    private HttpStatus status;
    private String code;
    private String reason;
}
