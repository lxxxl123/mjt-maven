package com.chen.sap.anno;

import cn.hutool.core.annotation.Alias;

import java.lang.annotation.*;

/**
 * @author chenwh3
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface SapTable {



    String name();

    /**
     * 获取的结果是否 trim
     */
    boolean trim() default true;
}
