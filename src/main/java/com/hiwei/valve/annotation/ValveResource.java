package com.hiwei.valve.annotation;

import com.hiwei.valve.controller.ValveType;
import com.hiwei.valve.exception.DefaultBlockExceptionHandler;
import com.hiwei.valve.exception.DefaultFallBackHandler;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ValveResource {
    ValveType valveType() default ValveType.REJECT;

    int value() default -1;

    int timeout() default -1;

    Class<?>[] blockHandlerClass() default {DefaultBlockExceptionHandler.class};

    Class<?>[] fallbackClass() default {DefaultFallBackHandler.class};
}
