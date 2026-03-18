package com.easyadmin.module.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.sys.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置Mapper
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfigEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}