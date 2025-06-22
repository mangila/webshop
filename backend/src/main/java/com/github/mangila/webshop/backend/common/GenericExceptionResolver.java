package com.github.mangila.webshop.backend.common;

import com.github.mangila.webshop.backend.common.util.exception.CommandException;
import com.github.mangila.webshop.backend.common.util.exception.DatabaseException;
import com.github.mangila.webshop.backend.common.util.exception.QueryException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class GenericExceptionResolver extends DataFetcherExceptionResolverAdapter {

    private static final Logger log = LoggerFactory.getLogger(GenericExceptionResolver.class);

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        return switch (ex) {
            case CommandException ce -> handleCommandException(ce, env);
            case QueryException qe -> handleQueryException(qe, env);
            case BindException be -> handleBindException(be, env);
            case ConstraintViolationException cve -> handleConstraintViolationException(cve, env);
            case DatabaseException dbe -> handleDatabaseException(dbe, env);
            default -> {
                log.error("ERR", ex);
                yield GraphqlErrorBuilder.newError(env)
                        .errorType(ErrorType.INTERNAL_ERROR)
                        .message("Something went wrong")
                        .build();
            }
        };
    }


    private GraphQLError handleDatabaseException(DatabaseException dbe, DataFetchingEnvironment env) {
        log.error("ERR", dbe);
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.INTERNAL_ERROR)
                .message("Database operation failed")
                .extensions(Map.of(
                        "resource", dbe.getResource().getSimpleName(),
                        "params", dbe.getParams()))
                .build();
    }

    private GraphQLError handleConstraintViolationException(ConstraintViolationException cve, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .errorType(ErrorType.BAD_REQUEST)
                .message("Validation error")
                .build();
    }

    private GraphQLError handleBindException(BindException be, DataFetchingEnvironment env) {
        Map<String, Object> errs = be.getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        ObjectError::getObjectName,
                        DefaultMessageSourceResolvable::getDefaultMessage
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
