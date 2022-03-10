package com.hiwei.valve.annotation;

import com.hiwei.valve.controller.ValveType;
import com.hiwei.valve.exception.DefaultBlockExceptionHandler;
import com.hiwei.valve.exception.DefaultFallBackHandler;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ValveResource {
    /**
     * 限流类型
     * @return
     */
    ValveType valveType() default ValveType.REJECT;

    /**
     * 阈值
     * @return
     */
    int value() default -1;

    /**
     * 超时时间，排队等待使用
     * @return
     */
    int timeout() default -1;

    /**
     * 限流处理handler
     * @return
     */
    Class<?>[] blockHandlerClass() default {DefaultBlockExceptionHandler.class};

    /**
     * 异常兜底handler
     * @return
     */
    Class<?>[] fallbackClass() default {DefaultFallBackHandler.class};
}
