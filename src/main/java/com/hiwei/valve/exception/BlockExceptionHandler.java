package com.hiwei.valve.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 限流异常处理接口
 * @author
 */
public interface BlockExceptionHandler {
    void handle(HttpServletRequest res, HttpServletResponse rsp, BlockException e) throws Exception;
}
