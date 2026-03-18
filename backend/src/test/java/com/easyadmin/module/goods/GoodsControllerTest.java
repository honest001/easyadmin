package com.easyadmin.module.goods;

import com.easyadmin.module.goods.controller.GoodsController;
import com.easyadmin.module.goods.service.GoodsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(GoodsController.class)
public class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsService goodsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        String requestBody = "{\"goodsName\":\"测试货品\",\"categoryId\":1,\"goodsSpec\":\"测试规格\",\"price\":100,\"costPrice\":80,\"warnStock\":5}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/goods/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInfo() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/info/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateStatus() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/goods/updateStatus")
                .param("id", "1")
                .param("status", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testStockList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/stock/list"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
