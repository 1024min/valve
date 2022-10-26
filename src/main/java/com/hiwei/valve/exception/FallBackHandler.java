package com.hiwei.valve.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 异常兜底接口
 * @author
 */
public interface FallBackHandler {
    void handle(HttpServletRequest res, HttpServletResponse rsp, Exception e) throws Exception;
}
