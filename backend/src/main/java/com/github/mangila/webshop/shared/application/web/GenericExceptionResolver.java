package com.github.mangila.webshop.shared.application.web;

import com.github.mangila.webshop.shared.domain.exception.WebException;
import com.github.mangila.webshop.shared.domain.exception.CommandException;
import com.github.mangila.webshop.shared.domain.exception.QueryException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
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
            case CommandException ce -> handleCommandException(ce, env);
            case QueryException qe -> handleQueryException(qe, env);
            case WebException ae -> handleApiException(ae, env);
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

    private GraphQLError handleApiException(WebException ex, DataFetchingEnvironment env) {
        ErrorType graphqlErrorType = ErrorType.BAD_REQUEST;
        if (ex.getHttpStatus().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            graphqlErrorType = ErrorType.NOT_FOUND;
        }
        return GraphqlErrorBuilder.newError(env)
                .errorType(graphqlErrorType)
                .message(ex.getMessage())
                .extensions(Map.of("resource", ex.getResource().getSimpleName()))
                .build();
    }

    private GraphQLError handleConstraintViolationException(ConstraintViolationException cve, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Validation error")
                .build();
    }

    private GraphQLError handleBindException(BindException be, DataFetchingEnvironment env) {
        Map<String, Object> errs = be.getFieldErrors()
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

    private GraphQLError handleQueryException(QueryException ex, DataFetchingEnvironment env) {
        ErrorType graphqlErrorType = ErrorType.BAD_REQUEST;
        if (ex.getHttpStatus().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            graphqlErrorType = ErrorType.NOT_FOUND;
        }
        return GraphqlErrorBuilder.newError(env)
                .errorType(graphqlErrorType)
                .message(ex.getMessage())
                .extensions(Map.of(
                        "resource", ex.getResource().getSimpleName(),
                        "query", ex.getQuery().getSimpleName())
                )
                .build();
    }

    private GraphQLError handleCommandException(CommandException ex, DataFetchingEnvironment env) {
        ErrorType graphqlErrorType = ErrorType.BAD_REQUEST;
        if (ex.getHttpStatus().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            graphqlErrorType = ErrorType.NOT_FOUND;
        }
        return GraphqlErrorBuilder.newError(env)
                .errorType(graphqlErrorType)
                .message(ex.getMessage())
                .extensions(Map.of(
                        "resource", ex.getResource().getSimpleName(),
                        "command", ex.getCommand().getSimpleName())
                )
                .build();
    }
}
