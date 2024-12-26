package com.ticket.webflux.exception.advice;

import com.ticket.webflux.exception.ApplicationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ApplicationAdvice {

    @ExceptionHandler(ApplicationException.class)
    public Mono<ResponseEntity<ServerExceptionResponse>> handleApplicationException(ApplicationException ex) {
        return Mono.just(ResponseEntity
                .status(ex.getStatus())
                .body(new ServerExceptionResponse(ex.getCode(), ex.getReason())));

    }

    public record ServerExceptionResponse(String code, String reason){

    }
}
