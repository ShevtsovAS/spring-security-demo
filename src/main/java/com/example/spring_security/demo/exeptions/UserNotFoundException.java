package com.example.spring_security.demo.exeptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(BAD_REQUEST)
public class UserNotFoundException extends NoSuchElementException {
    public UserNotFoundException(String s) {
        super(s);
    }
}
