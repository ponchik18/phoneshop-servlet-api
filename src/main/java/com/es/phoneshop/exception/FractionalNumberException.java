package com.es.phoneshop.exception;

public class FractionalNumberException extends Exception {
    @Override
    public String getMessage() {
        return "Fraction number not allowed!";
    }
}
