package com.github.mangila.webshop.app.error;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GenericExceptionResolver extends DataFetcherExceptionResolverAdapter {

    private static final Logger log = LoggerFactory.getLogger(GenericExceptionResolver.class);

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return switch (ex) {
            case ApplicationException ae -> handleApplicationException(ae, env);
            case ResourceNotFoundException rnfe -> handleResourceNotFoundException(rnfe, env);
            case BindException be -> handleBindException(be, env);
            case ConstraintViolationException cve -> handleConstraintViolationException(cve, env);
            default -> {
                log.error("ERR", ex);
                yield GraphqlErrorBuilder.newError(env)
                        .errorType(ErrorType.INTERNAL_ERROR)
                        .message("Something went wrong")
                        .build();
            }
        };
    }

    private GraphQLError handleApplicationException(ApplicationException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message(ex.getMessage())
                .build();
    }

    private GraphQLError handleResourceNotFoundException(ResourceNotFoundException ex, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.NOT_FOUND)
                .message(ex.getMessage())
                .extensions(Map.of(
                        "resource", ex.resource(),
                        "identifier", ex.identifier()))
                .build();
    }

    private GraphQLError handleConstraintViolationException(ConstraintViolationException ex, DataFetchingEnvironment env) {
        var errors = ex.getConstraintViolations().stream()
                .map(cv -> String.join(":", cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Validation error")
                .extensions(Map.of("errors", errors))
                .build();
    }

    private GraphQLError handleBindException(BindException ex, DataFetchingEnvironment env) {
        Map<String, Object> errs = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> String.join(":",
                                fe.getDefaultMessage(),
                                fe.getRejectedValue().toString())
                ));
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Bind error")
                .extensions(errs)
                .build();
    }
}
