package com.github.mangila.webshop.backend.common;

import com.github.mangila.webshop.backend.common.util.exception.DatabaseOperationFailedException;
import com.github.mangila.webshop.backend.common.util.exception.RequestedResourceNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.Map;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class GenericExceptionResolver extends DataFetcherExceptionResolverAdapter {

    private static final Logger log = LoggerFactory.getLogger(GenericExceptionResolver.class);

    private final Map<Class<?>, Tuple2<String, ErrorType>> errors = Map.of(
            RequestedResourceNotFoundException.class, Tuple.of("REQUESTED_RESOURCE_NOT_FOUND", ErrorType.NOT_FOUND),
            BindException.class, Tuple.of("FAULTY_REQUEST_BODY", ErrorType.BAD_REQUEST),
            ConstraintViolationException.class, Tuple.of("VALIDATION_ERROR", ErrorType.BAD_REQUEST),
            DatabaseOperationFailedException.class, Tuple.of("DATABASE_ERROR", ErrorType.INTERNAL_ERROR)
    );

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        var graphqlErr = errors.get(ex.getClass());
        if (graphqlErr == null) {
            log.error("ERR", ex);
            return GraphqlErrorBuilder.newError(env)
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .message("Something went wrong")
                    .build();
        }
        if (graphqlErr._2 == ErrorType.INTERNAL_ERROR) {
            log.error("ERR", ex);
        }
        return GraphqlErrorBuilder.newError(env)
                .errorType(graphqlErr._2)
                .message(ex.getMessage())
                .extensions(Map.of("code", graphqlErr._1))
                .build();
    }
}
