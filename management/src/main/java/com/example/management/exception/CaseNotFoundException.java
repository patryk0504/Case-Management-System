package com.example.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CaseNotFoundException extends ResponseStatusException {
    public CaseNotFoundException(Long id){
        super(HttpStatus.NOT_FOUND, "Couldn't find case with id: " + id);
    }
    public CaseNotFoundException(String id){
        super(HttpStatus.NOT_FOUND, "Couldn't find case with id: " + id);
    }

}
