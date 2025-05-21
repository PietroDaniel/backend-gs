package com.pietro.backendgs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GsException extends ResponseStatusException {
    
    public GsException(String message, HttpStatus status) {
        super(status, message);
    }
} 