package com.github.mangila.webshop.app.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public static final String LRU = "lru";
    public static final String TTL = "ttl";

    @Bean
    public CacheManager cacheManager(MeterRegistry meterRegistry) {
        var caffeineCacheManager = new CaffeineCacheManager() {
            @Override
            protected CaffeineCache createCaffeineCache(String name) {
                Caffeine<Object, Object> caffeineBuilder = switch (name) {
                    case LRU -> Caffeine.newBuilder()
                            .maximumSize(1000)
                            .expireAfterAccess(10, TimeUnit.MINUTES)
                            .recordStats();

                    case TTL -> Caffeine.newBuilder()
                            .expireAfterWrite(1, TimeUnit.HOURS)
                            .maximumSize(1000)
                            .recordStats();

                    default -> Caffeine.newBuilder()
                            .expireAfterWrite(30, TimeUnit.MINUTES)
                            .maximumSize(200)
                            .recordStats();
                };

                Cache<Object, Object> nativeCache = caffeineBuilder.build();
                CaffeineCacheMetrics.monitor(meterRegistry, nativeCache, name);
                return new CaffeineCache(name, nativeCache);
            }
        };
        caffeineCacheManager.setCacheNames(List.of(LRU, TTL, "default"));
        return caffeineCacheManager;
    }
}