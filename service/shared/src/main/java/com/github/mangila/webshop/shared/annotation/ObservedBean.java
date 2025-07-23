package com.github.mangila.webshop.shared.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ObservedBean {

    String name() default "";

    String[] tags() default {};
}
