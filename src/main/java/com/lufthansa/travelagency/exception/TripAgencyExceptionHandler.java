package com.lufthansa.travelagency.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TripAgencyExceptionHandler {

    @ExceptionHandler(value = TripAgencyApplicationException.class)
    public ResponseEntity<Object> exception(TripAgencyApplicationException exception) {
        return new ResponseEntity<>(exception, exception.getStatus());
    }
}
