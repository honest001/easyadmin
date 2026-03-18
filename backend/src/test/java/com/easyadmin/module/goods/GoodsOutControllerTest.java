package com.easyadmin.module.goods;

import com.easyadmin.module.goods.controller.GoodsOutController;
import com.easyadmin.module.goods.service.GoodsOutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(GoodsOutController.class)
public class GoodsOutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsOutService goodsOutService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        String requestBody = "{\"outType\":1,\"items\":[{\"goodsId\":1,\"goodsNum\":5}]}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/goods/out/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/out/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInfo() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/out/info/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
