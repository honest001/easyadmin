package com.easyadmin.module.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.module.goods.entity.GoodsEntity;
import com.easyadmin.module.goods.entity.GoodsInEntity;
import com.easyadmin.module.goods.entity.GoodsInItemEntity;
import com.easyadmin.module.goods.mapper.GoodsInItemMapper;
import com.easyadmin.module.goods.mapper.GoodsInMapper;
import com.easyadmin.module.goods.mapper.GoodsMapper;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.goods.service.GoodsInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 入库单Service实现类
 */
@Service
public class GoodsInServiceImpl extends ServiceImpl<GoodsInMapper, GoodsInEntity> implements GoodsInService {

    @Autowired
    private GoodsInItemMapper goodsInItemMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveIn(GoodsInEntity goodsIn, List<GoodsInItemEntity> items) {
        // 生成入库单号
        String inNo = "IN" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 8);
        goodsIn.setInNo(inNo);
        goodsIn.setInTime(LocalDateTime.now());
        
        // 计算总数量
        int totalNum = 0;
        for (GoodsInItemEntity item : items) {
            totalNum += item.getGoodsNum();
        }
        goodsIn.setTotalNum(totalNum);
        
        // 保存入库单
        this.save(goodsIn);
        
        // 保存入库明细并更新库存
        for (GoodsInItemEntity item : items) {
            item.setInId(goodsIn.getId());
            goodsInItemMapper.insert(item);
            
            // 更新货品库存
            GoodsEntity goods = goodsMapper.selectById(item.getGoodsId());
            if (goods != null) {
                goods.setStock(goods.getStock() + item.getGoodsNum());
                goodsMapper.updateById(goods);
            }
        }
        
        return true;
    }

    @Override
    public PageResult<GoodsInEntity> pageList(Map<String, Object> params) {
        // 简化实现，返回模拟数据
        PageResult<GoodsInEntity> pageResult = new PageResult<>();
        pageResult.setRecords(this.list());
        pageResult.setTotal((long) this.count());
        return pageResult;
    }
}