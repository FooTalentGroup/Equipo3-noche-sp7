package com.stockia.stockia.exceptions.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientProtectedException extends RuntimeException {
    public ClientProtectedException(String message) {
        super(message);
    }
}
