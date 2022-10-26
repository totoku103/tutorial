package me.totoku103.tutorial.authorization.handler;

import jdk.jfr.Experimental;
import lombok.extern.slf4j.Slf4j;
import me.totoku103.tutorial.authorization.exception.NotFoundDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundDataException.class)
    public ResponseEntity notFoundDataException(NotFoundDataException exception) {
        log.error("exception: {}", exception.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = UnauthorizedClientException.class)
    public ResponseEntity unauthorizedClientException(UnauthorizedClientException exception) {
        log.error("exception: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
