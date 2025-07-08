package com.github.mangila.webshop.shared.infrastructure.spring.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ObservedBean
@Service
public @interface ObservedService {

    @AliasFor(annotation = Service.class, attribute = "value")
    String value() default "";

    @AliasFor(annotation = ObservedBean.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = ObservedBean.class, attribute = "tags")
    String[] tags() default {};
}

