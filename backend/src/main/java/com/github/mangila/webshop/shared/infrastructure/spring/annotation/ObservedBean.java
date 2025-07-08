package com.github.mangila.webshop.shared.infrastructure.spring.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ObservedBean {

    String name() default "";

    String[] tags() default {};
}
