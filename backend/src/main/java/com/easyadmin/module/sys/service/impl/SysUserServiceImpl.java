package com.easyadmin.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.constant.CommonConstant;
import com.easyadmin.common.exception.BusinessException;
import com.easyadmin.common.util.SecurityUtil;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.sys.entity.SysUserEntity;
import com.easyadmin.module.sys.mapper.SysUserMapper;
import com.easyadmin.module.sys.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 系统用户Service实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Override
    public PageResult<SysUserEntity> pageList(Map<String, Object> params) {
        // 构建分页参数，兼容前端传递的page和limit参数
        int pageNum = Integer.parseInt(params.getOrDefault("page", params.getOrDefault("pageNum", 1)).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("limit", params.getOrDefault("pageSize", 10)).toString());
        Page<SysUserEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<>();
        wrapper.like(params.containsKey("username"), "username", params.get("username"));
        wrapper.like(params.containsKey("realName"), "real_name", params.get("realName"));
        wrapper.orderByDesc("create_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<SysUserEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateUser(SysUserEntity user) {
        // 新增操作：密码加密
        if (user.getId() == null) {
            user.setPassword(SecurityUtil.bCryptEncode(user.getPassword()));
            user.setCreateTime(LocalDateTime.now());
        } else {
            // 编辑操作：如果密码为空，保持原密码不变
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                SysUserEntity existingUser = this.getById(user.getId());
                user.setPassword(existingUser.getPassword());
            } else {
                // 如果密码不为空，加密后更新
                user.setPassword(SecurityUtil.bCryptEncode(user.getPassword()));
            }
        }
        // 编辑操作：允许修改超级管理员的非敏感信息
        if (CommonConstant.ADMIN_ID.equals(user.getId())) {
            // 只允许修改姓名和电话，其他字段保持不变
            SysUserEntity existingUser = this.getById(user.getId());
            user.setUsername(existingUser.getUsername()); // 保持用户名不变
            user.setRoleId(existingUser.getRoleId()); // 保持角色不变
            user.setStatus(existingUser.getStatus()); // 保持状态不变
        }
        user.setUpdateTime(LocalDateTime.now());
        return this.saveOrUpdate(user);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        // 禁止禁用超级管理员
        if (CommonConstant.ADMIN_ID.equals(id)) {
            throw new BusinessException("不允许禁用超级管理员");
        }
        SysUserEntity user = new SysUserEntity();
        user.setId(id);
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    public boolean updatePassword(Long id, String oldPassword, String newPassword) {
        // 禁止修改超级管理员密码（可通过其他方式）
        if (CommonConstant.ADMIN_ID.equals(id)) {
            throw new BusinessException("超级管理员密码修改请联系系统管理员");
        }
        // 验证旧密码
        SysUserEntity user = this.getById(id);
        if (!SecurityUtil.bCryptMatch(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        // 更新密码
        user.setPassword(SecurityUtil.bCryptEncode(newPassword));
        user.setUpdateTime(LocalDateTime.now());
        return this.updateById(user);
    }

    @Override
    public SysUserEntity login(String username, String password) {
        // 根据用户名查询用户
        QueryWrapper<SysUserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        SysUserEntity user = this.getOne(wrapper);
        
        // 验证用户是否存在
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 验证用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 验证密码（支持明文和BCrypt）
        String dbPassword = user.getPassword();
        if (dbPassword.startsWith("$2a$") || dbPassword.startsWith("$2b$") || dbPassword.startsWith("$2y$")) {
            // 使用BCrypt验证
            if (!SecurityUtil.bCryptMatch(password, dbPassword)) {
                throw new BusinessException("用户名或密码错误");
            }
        } else {
            // 直接比较明文密码
            if (!password.equals(dbPassword)) {
                throw new BusinessException("用户名或密码错误");
            }
        }
        
        return user;
    }

    @Override
    public boolean deleteUser(Long id) {
        // 禁止删除超级管理员
        if (CommonConstant.ADMIN_ID.equals(id)) {
            throw new BusinessException("不允许删除超级管理员");
        }
        return this.removeById(id);
    }
}