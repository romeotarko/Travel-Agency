package com.lufthansa.travelagency.exception;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties({"cause", "stackTrace", "message", "suppressed", "localizedMessage"})
public class TripAgencyApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    @JsonIgnore
    private HttpStatus status;

}
