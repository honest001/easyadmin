package com.easyadmin.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.constant.CommonConstant;
import com.easyadmin.module.sys.entity.SysRoleEntity;
import com.easyadmin.module.sys.mapper.SysRoleMapper;
import com.easyadmin.module.sys.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统角色Service实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {

    @Override
    public List<SysRoleEntity> listAll() {
        QueryWrapper<SysRoleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", CommonConstant.STATUS_NORMAL);
        wrapper.orderByAsc("id");
        return this.list(wrapper);
    }
}