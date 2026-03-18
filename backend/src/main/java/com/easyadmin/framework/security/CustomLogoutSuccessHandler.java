package com.easyadmin.framework.security;

import com.easyadmin.common.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出成功处理类
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 构建退出成功响应
        Result<String> result = Result.success("退出成功");
        // 设置响应头
        response.setContentType("application/json;charset=utf-8");
        // 输出响应
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}