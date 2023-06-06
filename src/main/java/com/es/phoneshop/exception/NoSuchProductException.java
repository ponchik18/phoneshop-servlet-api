package com.es.phoneshop.exception;

import java.util.UUID;

public class NoSuchProductException extends NoSuchElementException {

    public NoSuchProductException(UUID id) {
        super(id);
    }

}

