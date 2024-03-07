package com.restaurant.menuservice.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CustomServiceException extends RuntimeException {
    private final transient Object data;

    public CustomServiceException(String message, Object data) {
        super(message);
        this.data = data;
        log.error("CustomServiceException occurs.");
    }
}
