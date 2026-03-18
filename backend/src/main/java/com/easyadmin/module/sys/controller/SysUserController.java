package com.easyadmin.module.sys.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.sys.entity.SysUserEntity;
import com.easyadmin.module.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 系统用户Controller
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 账号分页列表
     */
    @GetMapping("/list")
    public Result<PageResult<SysUserEntity>> list(@RequestParam Map<String, Object> params) {
        PageResult<SysUserEntity> pageResult = sysUserService.pageList(params);
        return Result.success(pageResult);
    }

    /**
     * 账号新增/编辑
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody SysUserEntity user) {
        sysUserService.saveOrUpdateUser(user);
        return Result.success();
    }

    /**
     * 账号禁用/启用
     */
    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestBody Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Integer status = Integer.valueOf(params.get("status").toString());
        sysUserService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 根据ID查询账号详情
     */
    @GetMapping("/info/{id}")
    public Result<Map<String, Object>> info(@PathVariable Long id) {
        // 使用JdbcTemplate直接查询数据库，确保密码字段被获取
        String sql = "SELECT id, username, password, real_name as realName, phone, role_id as roleId, status, create_time as createTime, update_time as updateTime, remark FROM sys_user WHERE id = ?";
        Map<String, Object> userInfo = jdbcTemplate.queryForMap(sql, id);
        return Result.success(userInfo);
    }

    /**
     * 个人密码修改
     */
    @PostMapping("/updatePwd")
    public Result<Void> updatePwd(@RequestParam Long id, @RequestParam String oldPwd, @RequestParam String newPwd) {
        sysUserService.updatePassword(id, oldPwd, newPwd);
        return Result.success();
    }
    
    /**
     * 测试接口，返回硬编码的密码
     */
    @GetMapping("/test")
    public Result<Map<String, Object>> test() {
        Map<String, Object> testData = new HashMap<>();
        testData.put("id", 1);
        testData.put("username", "admin");
        testData.put("password", "123456"); // 硬编码的密码
        testData.put("status", 1);
        return Result.success(testData);
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestBody Map<String, Long> params) {
        Long id = params.get("id");
        sysUserService.deleteUser(id);
        return Result.success();
    }
}