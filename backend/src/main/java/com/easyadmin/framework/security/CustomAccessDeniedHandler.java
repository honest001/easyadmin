package com.easyadmin.framework.security;

import com.easyadmin.common.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义权限拒绝处理类
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 构建权限拒绝响应
        Result<String> result = Result.fail("权限不足，无法访问该资源");
        // 设置响应头
        response.setContentType("application/json;charset=utf-8");
        // 输出响应
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}