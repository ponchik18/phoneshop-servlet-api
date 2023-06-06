package com.es.phoneshop.exception;

import java.util.UUID;

public class NoSuchOrderException extends NoSuchElementException {

    public NoSuchOrderException(UUID id) {
        super(id);
    }

}

