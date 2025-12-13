package com.stockia.stockia.exceptions.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientInactiveException extends RuntimeException {
    public ClientInactiveException(String message) {
        super(message);
    }
}
