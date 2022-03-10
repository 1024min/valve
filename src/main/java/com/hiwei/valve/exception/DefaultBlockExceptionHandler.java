package com.hiwei.valve.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class DefaultBlockExceptionHandler implements BlockExceptionHandler{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        response.setStatus(429);
        StringBuffer url = request.getRequestURL();
        if ("GET".equals(request.getMethod()) && !isBlank(request.getQueryString())) {
            url.append("?").append(request.getQueryString());
        }

        PrintWriter out = response.getWriter();
        out.print("Blocked by Sentinel (flow limiting)");
        out.flush();
        out.close();
    }
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }
}
