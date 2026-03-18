package com.easyadmin.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.user.entity.AttendanceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 考勤打卡Mapper
 */
@Mapper
public interface AttendanceMapper extends BaseMapper<AttendanceEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}