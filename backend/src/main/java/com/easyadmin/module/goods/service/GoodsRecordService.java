package com.easyadmin.module.goods.service;

import com.easyadmin.common.vo.PageResult;
import java.util.Map;

/**
 * 出入库记录Service
 */
public interface GoodsRecordService {
    /**
     * 分页查询出入库明细
     */
    PageResult<Map<String, Object>> pageList(Map<String, Object> params);
}
