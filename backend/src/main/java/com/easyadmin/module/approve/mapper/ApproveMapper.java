package com.easyadmin.module.approve.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.approve.entity.ApproveEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 审批申请Mapper
 */
@Mapper
public interface ApproveMapper extends BaseMapper<ApproveEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}