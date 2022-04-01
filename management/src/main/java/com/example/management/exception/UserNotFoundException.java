package com.example.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {
    public UserNotFoundException(Long id){
        super(HttpStatus.NOT_FOUND, "Couldn't find user with id: " + id);
    }
}
