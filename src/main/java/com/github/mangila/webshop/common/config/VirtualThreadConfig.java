package com.github.mangila.webshop.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class VirtualThreadConfig {

    @Bean
    public ThreadFactory virtualThreadFactory() {
        return Thread.ofVirtual().factory();
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService virtualThreadExecutor(ThreadFactory virtualThreadFactory) {
        return Executors.newThreadPerTaskExecutor(virtualThreadFactory);
    }
}
