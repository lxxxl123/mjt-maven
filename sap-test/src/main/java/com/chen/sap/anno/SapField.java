package com.chen.sap.anno;

import java.lang.annotation.*;

/**
 * @author chenwh3
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SapField {
    String name() default "";

    boolean ignore() default false;

    boolean in() default true;

    int addZero() default -1;

}
