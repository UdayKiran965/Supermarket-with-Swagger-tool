package com.uday.Exception;

public class CheckoutInProgressException extends RuntimeException {
    public CheckoutInProgressException(String message) {
        super(message);
    }
}

