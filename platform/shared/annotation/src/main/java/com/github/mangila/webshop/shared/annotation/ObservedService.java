package com.github.mangila.webshop.shared.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ObservedStereotype
@Service
public @interface ObservedService {

    @AliasFor(annotation = Service.class, attribute = "value")
    String value() default "";

    @AliasFor(annotation = ObservedStereotype.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = ObservedStereotype.class, attribute = "tags")
    String[] tags() default {};
}

