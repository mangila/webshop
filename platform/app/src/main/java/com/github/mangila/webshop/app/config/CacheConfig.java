package com.github.mangila.webshop.app.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.mangila.webshop.shared.util.CacheName;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static com.github.mangila.webshop.shared.util.CacheName.*;

@Configuration
@EnableCaching
public class CacheConfig {

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

                    case EVENT_REGISTRY, DOMAIN_REGISTRY -> Caffeine.newBuilder()
                            .initialCapacity(50)
                            .recordStats();
                    default -> throw new IllegalStateException("Unexpected value: " + name);
                };

                Cache<Object, Object> nativeCache = caffeineBuilder.build();
                CaffeineCacheMetrics.monitor(meterRegistry, nativeCache, name);
                return new CaffeineCache(name, nativeCache);
            }
        };
        caffeineCacheManager.setCacheNames(CacheName.ALL);
        return caffeineCacheManager;
    }
}