package com.github.mangila.webshop.shared.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@ObservedStereotype
@Component
public @interface ObservedComponent {

    @AliasFor(annotation = Component.class, attribute = "value")
    String value() default "";

    @AliasFor(annotation = ObservedStereotype.class, attribute = "name")
    String name() default "";

    @AliasFor(annotation = ObservedStereotype.class, attribute = "tags")
    String[] tags() default {};
}
