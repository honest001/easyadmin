package com.easyadmin.module.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.exception.BusinessException;
import com.easyadmin.module.goods.entity.GoodsEntity;
import com.easyadmin.module.goods.entity.GoodsOutEntity;
import com.easyadmin.module.goods.entity.GoodsOutItemEntity;
import com.easyadmin.module.goods.mapper.GoodsMapper;
import com.easyadmin.module.goods.mapper.GoodsOutItemMapper;
import com.easyadmin.module.goods.mapper.GoodsOutMapper;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.goods.service.GoodsOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 出库单Service实现类
 */
@Service
public class GoodsOutServiceImpl extends ServiceImpl<GoodsOutMapper, GoodsOutEntity> implements GoodsOutService {

    @Autowired
    private GoodsOutItemMapper goodsOutItemMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOut(GoodsOutEntity goodsOut, List<GoodsOutItemEntity> items) {
        // 生成出库单号
        String outNo = "OUT" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 8);
        goodsOut.setOutNo(outNo);
        goodsOut.setOutTime(LocalDateTime.now());
        
        // 计算总数量
        int totalNum = 0;
        for (GoodsOutItemEntity item : items) {
            totalNum += item.getGoodsNum();
        }
        goodsOut.setTotalNum(totalNum);
        
        // 检查库存是否充足
        for (GoodsOutItemEntity item : items) {
            GoodsEntity goods = goodsMapper.selectById(item.getGoodsId());
            if (goods == null) {
                throw new BusinessException("货品不存在");
            }
            if (goods.getStock() < item.getGoodsNum()) {
                throw new BusinessException("货品库存不足：" + goods.getGoodsName());
            }
        }
        
        // 保存出库单
        this.save(goodsOut);
        
        // 保存出库明细并更新库存
        for (GoodsOutItemEntity item : items) {
            item.setOutId(goodsOut.getId());
            goodsOutItemMapper.insert(item);
            
            // 更新货品库存
            GoodsEntity goods = goodsMapper.selectById(item.getGoodsId());
            if (goods != null) {
                goods.setStock(goods.getStock() - item.getGoodsNum());
                goodsMapper.updateById(goods);
            }
        }
        
        return true;
    }

    @Override
    public PageResult<GoodsOutEntity> pageList(Map<String, Object> params) {
        // 简化实现，返回模拟数据
        PageResult<GoodsOutEntity> pageResult = new PageResult<>();
        pageResult.setRecords(this.list());
        pageResult.setTotal((long) this.count());
        return pageResult;
    }
}