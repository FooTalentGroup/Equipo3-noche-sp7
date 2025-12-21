package com.stockia.stockia.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserProtectedException extends RuntimeException {
    public UserProtectedException(String message) {
        super(message);
    }
}
