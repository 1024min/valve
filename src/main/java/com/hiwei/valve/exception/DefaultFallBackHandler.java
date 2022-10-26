package com.hiwei.valve.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 默认兜底异常handler
 * @author
 */
public class DefaultFallBackHandler implements FallBackHandler{
    @Override
    public void handle(HttpServletRequest res, HttpServletResponse rsp, Exception e) throws Exception {
        //返回json数据
        rsp.setStatus(500);
        rsp.setCharacterEncoding("utf-8");
        rsp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(rsp.getWriter(), e.getMessage());
    }
}
