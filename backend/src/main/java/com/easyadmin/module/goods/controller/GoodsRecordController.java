package com.easyadmin.module.goods.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.goods.service.GoodsRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 出入库记录Controller
 */
@RestController
@RequestMapping("/goods/record")
public class GoodsRecordController {

    @Autowired
    private GoodsRecordService goodsRecordService;

    /**
     * 出入库明细列表
     */
    @GetMapping("/list")
    public Result<PageResult<Map<String, Object>>> list(@RequestParam Map<String, Object> params) {
        PageResult<Map<String, Object>> pageResult = goodsRecordService.pageList(params);
        return Result.success(pageResult);
    }
}
