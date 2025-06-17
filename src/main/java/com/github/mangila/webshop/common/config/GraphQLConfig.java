package com.github.mangila.webshop.common.config;


import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.scalars.ExtendedScalars;
import graphql.schema.DataFetchingEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig extends DataFetcherExceptionResolverAdapter {

    private static final Logger log = LoggerFactory.getLogger(GraphQLConfig.class);

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.GraphQLBigInteger)
                .scalar(ExtendedScalars.Json);
    }

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {
        log.error("ERR", ex);
        if (ex instanceof RuntimeException e) {
            return GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
        }
        return GraphQLError.newError()
                .message("Internal Error")
                .errorType(ErrorType.INTERNAL_ERROR)
                .build();
    }
}
