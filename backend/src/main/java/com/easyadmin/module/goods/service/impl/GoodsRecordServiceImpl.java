package com.easyadmin.module.goods.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.goods.mapper.GoodsInItemMapper;
import com.easyadmin.module.goods.mapper.GoodsOutItemMapper;
import com.easyadmin.module.goods.service.GoodsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 出入库记录Service实现
 */
@Service
public class GoodsRecordServiceImpl implements GoodsRecordService {

    @Autowired
    private GoodsInItemMapper goodsInItemMapper;

    @Autowired
    private GoodsOutItemMapper goodsOutItemMapper;

    @Override
    public PageResult<Map<String, Object>> pageList(Map<String, Object> params) {
        // 这里简化实现，实际应该根据参数查询数据库
        // 模拟数据
        List<Map<String, Object>> records = new ArrayList<>();
        
        // 模拟入库记录
        Map<String, Object> record1 = Map.of(
            "id", 1,
            "recordType", "in",
            "orderNo", "IN20260101001",
            "goodsName", "笔记本电脑",
            "goodsSpec", "14英寸",
            "goodsNum", 5,
            "operatorName", "管理员",
            "operateTime", "2026-01-01 10:00:00",
            "remark", "采购入库"
        );
        
        Map<String, Object> record2 = Map.of(
            "id", 2,
            "recordType", "in",
            "orderNo", "IN20260101001",
            "goodsName", "打印机",
            "goodsSpec", "黑白激光",
            "goodsNum", 5,
            "operatorName", "管理员",
            "operateTime", "2026-01-01 10:00:00",
            "remark", "采购入库"
        );
        
        // 模拟出库记录
        Map<String, Object> record3 = Map.of(
            "id", 3,
            "recordType", "out",
            "orderNo", "OUT20260101001",
            "goodsName", "笔记本电脑",
            "goodsSpec", "14英寸",
            "goodsNum", 3,
            "operatorName", "管理员",
            "operateTime", "2026-01-01 14:00:00",
            "remark", "销售出库"
        );
        
        Map<String, Object> record4 = Map.of(
            "id", 4,
            "recordType", "out",
            "orderNo", "OUT20260102001",
            "goodsName", "矿泉水",
            "goodsSpec", "500ml",
            "goodsNum", 10,
            "operatorName", "管理员",
            "operateTime", "2026-01-02 09:00:00",
            "remark", "损耗出库"
        );
        
        Map<String, Object> record5 = Map.of(
            "id", 5,
            "recordType", "in",
            "orderNo", "IN20260103001",
            "goodsName", "文件夹",
            "goodsSpec", "A4",
            "goodsNum", 20,
            "operatorName", "管理员",
            "operateTime", "2026-01-03 10:00:00",
            "remark", "办公用品入库"
        );
        
        records.add(record1);
        records.add(record2);
        records.add(record3);
        records.add(record4);
        records.add(record5);
        
        PageResult<Map<String, Object>> pageResult = new PageResult<>();
    pageResult.setRecords(records);
    pageResult.setTotal((long) records.size());
        
        return pageResult;
    }
}
