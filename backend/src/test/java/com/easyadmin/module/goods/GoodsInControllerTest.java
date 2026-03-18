package com.easyadmin.module.goods;

import com.easyadmin.module.goods.controller.GoodsInController;
import com.easyadmin.module.goods.service.GoodsInService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(GoodsInController.class)
public class GoodsInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsInService goodsInService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        String requestBody = "{\"supplierId\":1,\"items\":[{\"goodsId\":1,\"goodsNum\":10}]}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/goods/in/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/in/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInfo() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/in/info/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
