package com.easyadmin.module.sys.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.sys.entity.SysRoleEntity;
import com.easyadmin.module.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色Controller
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 角色列表查询
     */
    @GetMapping("/list")
    public Result<List<SysRoleEntity>> list() {
        List<SysRoleEntity> roleList = sysRoleService.listAll();
        return Result.success(roleList);
    }

    /**
     * 保存角色
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody SysRoleEntity role) {
        sysRoleService.saveOrUpdate(role);
        return Result.success();
    }
}