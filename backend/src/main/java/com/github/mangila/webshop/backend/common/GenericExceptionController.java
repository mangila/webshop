package com.github.mangila.webshop.backend.common;

import com.github.mangila.webshop.backend.common.util.exception.DatabaseOperationFailedException;
import com.github.mangila.webshop.backend.common.util.exception.RequestedResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GenericExceptionController {

    private static final Logger log = LoggerFactory.getLogger(GenericExceptionController.class);

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFoundException(NoResourceFoundException ex, WebRequest request) {
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Path/Resource not found");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Failed");
        problem.setDetail("Input validation failed for the request");
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                        fieldError -> String.format("%s: %s", fieldError.getRejectedValue(), fieldError.getDefaultMessage())));
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(RequestedResourceNotFoundException.class)
    public ProblemDetail handleRequestedResourceNotFoundException(RequestedResourceNotFoundException ex, WebRequest request) {
        var problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Requested resource not found");
        problem.setDetail(ex.getMessage());
        return problem;
    }

    @ExceptionHandler(DatabaseOperationFailedException.class)
    public ProblemDetail handleDatabaseOperationFailedException(DatabaseOperationFailedException ex, WebRequest request) {
        log.error("ERR", ex);
        var problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Database operation failed");
        problem.setDetail("INTERNAL_SERVER_ERROR");
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex, WebRequest request) {
        log.error("ERR", ex);
        var problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("INTERNAL_SERVER_ERROR");
        problem.setDetail("Something went wrong");
        return problem;
    }
}
