package me.totoku103.tutorial.authorization.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundDataException extends Exception {
    private Object value;
    private final String message = "not found data. {}";

    private NotFoundDataException() {
    }

    public NotFoundDataException(Object value) {
        this.value = value;
    }
}
