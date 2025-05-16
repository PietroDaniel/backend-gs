package com.pietro.backendgs.exception;

public class ItemNameAlreadyExistsException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public ItemNameAlreadyExistsException(String message) {
        super(message);
    }
} 