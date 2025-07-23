package com.github.mangila.webshop.shared.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Repository;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ObservedBean
@Repository
public @interface ObservedRepository {

    @AliasFor(annotation = Repository.class, attribute = "value")
    String value() default "";

    @AliasFor(annotation = ObservedBean.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = ObservedBean.class, attribute = "tags")
    String[] tags() default {};
}

