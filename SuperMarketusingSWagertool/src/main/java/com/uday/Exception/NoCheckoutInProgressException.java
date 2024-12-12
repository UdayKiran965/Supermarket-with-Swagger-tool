package com.uday.Exception;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class NoCheckoutInProgressException extends RuntimeException {
    public NoCheckoutInProgressException(String message) {
        super(message);
    }
 // Add an exception handler for NoCheckoutInProgressException in your controller
    @ExceptionHandler(NoCheckoutInProgressException.class)
    public ResponseEntity<String> handleNoCheckoutInProgressException(NoCheckoutInProgressException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST_400).body(ex.getMessage());
    }

}

