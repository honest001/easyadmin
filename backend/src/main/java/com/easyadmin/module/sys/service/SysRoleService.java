package com.easyadmin.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.module.sys.entity.SysRoleEntity;

/**
 * 系统角色Service
 */
public interface SysRoleService extends IService<SysRoleEntity> {
    // 获取所有有效角色
    java.util.List<SysRoleEntity> listAll();
}