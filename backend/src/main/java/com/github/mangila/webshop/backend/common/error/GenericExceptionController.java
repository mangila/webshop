package com.github.mangila.webshop.backend.common.error;

import com.github.mangila.webshop.backend.common.error.exception.ApiException;
import com.github.mangila.webshop.backend.common.error.exception.CommandException;
import com.github.mangila.webshop.backend.common.error.exception.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Map;
import java.util.Objects;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        var problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("HTTP Message Not Readable");
        problem.setDetail("Bad format of the request body");
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
                .map(fieldError -> {
                    if (fieldError.isBindingFailure()) {
                        return fieldError.getField();
                    }
                    if (Objects.isNull(fieldError.getRejectedValue())) {
                        return String.join(":", fieldError.getField(), fieldError.getDefaultMessage());
                    }
                    return String.join(":", fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue().toString());
                }).toList();
        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(QueryException.class)
    public ProblemDetail handleQueryException(QueryException ex, WebRequest request) {
        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setTitle("Query Error");
        problem.setDetail(ex.getMessage());
        problem.setProperties(
                Map.of("resource", ex.getResource().getSimpleName(),
                        "query", ex.getQuery().getSimpleName())
        );
        return problem;
    }

    @ExceptionHandler(CommandException.class)
    public ProblemDetail handleCommandException(CommandException ex, WebRequest request) {
        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setTitle("Command Error");
        problem.setDetail(ex.getMessage());
        problem.setProperties(
                Map.of("resource", ex.getResource().getSimpleName(),
                        "command", ex.getCommand().getSimpleName())
        );
        return problem;
    }

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException ex, WebRequest request) {
        var problem = ProblemDetail.forStatus(ex.getHttpStatus());
        problem.setTitle("Api Error");
        problem.setDetail(ex.getMessage());
        problem.setProperties(Map.of("resource", ex.getResource().getSimpleName()));
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
