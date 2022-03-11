package com.hiwei.valve.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BlockExceptionHandler {
    void handle(HttpServletRequest res, HttpServletResponse rsp, BlockException e) throws Exception;
}
