package com.es.phoneshop.exception;

public class NegativeNumberException extends Exception {
    @Override
    public String getMessage() {
        return "0 or negative not allowed";
    }
}
