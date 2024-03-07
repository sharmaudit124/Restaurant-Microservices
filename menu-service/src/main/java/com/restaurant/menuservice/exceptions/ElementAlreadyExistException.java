package com.restaurant.menuservice.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class ElementAlreadyExistException extends RuntimeException {
    private final transient Object data;

    public ElementAlreadyExistException(String message, Object data) {
        super(message);
        this.data = data;

        log.error("ElementAlreadyExistException error occurs.");
    }
}
