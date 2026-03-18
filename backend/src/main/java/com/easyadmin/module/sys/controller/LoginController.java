package com.easyadmin.module.sys.controller;

import com.easyadmin.common.exception.BusinessException;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.sys.entity.SysUserEntity;
import com.easyadmin.module.sys.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 登录控制器
 */
@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, Object> loginForm) {
        logger.info("Login request received: {}", loginForm);
        
        String username = null;
        String password = null;
        
        // 尝试获取username和password
        if (loginForm.containsKey("username")) {
            Object usernameObj = loginForm.get("username");
            if (usernameObj instanceof String) {
                username = (String) usernameObj;
            } else if (usernameObj != null) {
                username = usernameObj.toString();
            }
        }
        
        if (loginForm.containsKey("password")) {
            Object passwordObj = loginForm.get("password");
            if (passwordObj instanceof String) {
                password = (String) passwordObj;
            } else if (passwordObj != null) {
                password = passwordObj.toString();
            }
        }
        
        logger.info("Username: {}, Password: {}", username, password);
        
        try {
            // 调用服务层进行登录验证
            SysUserEntity user = sysUserService.login(username, password);
            
            // 登录成功，返回token和用户信息
            Map<String, Object> data = Map.of(
                "token", "test-token",
                "user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "realName", user.getRealName()
                )
            );
            
            logger.info("Login successful for user: {}", username);
            return Result.success(data);
        } catch (BusinessException e) {
            logger.error("Login failed for user {}: {}", username, e.getMessage());
            return Result.fail(e.getMessage());
        }
    }
}
