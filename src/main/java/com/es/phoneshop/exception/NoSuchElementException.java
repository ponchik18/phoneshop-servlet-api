package com.es.phoneshop.exception;

import java.util.UUID;

public class NoSuchElementException extends RuntimeException {

    private final UUID id;

    public NoSuchElementException(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

}

