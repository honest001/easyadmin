package com.easyadmin.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.sys.entity.SysUserEntity;
import java.util.Map;

/**
 * 系统用户Service
 */
public interface SysUserService extends IService<SysUserEntity> {
    // 分页查询账号列表
    PageResult<SysUserEntity> pageList(Map<String, Object> params);
    // 新增/编辑账号
    boolean saveOrUpdateUser(SysUserEntity user);
    // 修改账号状态
    boolean updateStatus(Long id, Integer status);
    // 修改密码
    boolean updatePassword(Long id, String oldPassword, String newPassword);
    // 用户登录验证
    SysUserEntity login(String username, String password);
    // 删除用户
    boolean deleteUser(Long id);
}