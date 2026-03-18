package com.easyadmin.module.goods;

import com.easyadmin.module.goods.controller.GoodsCategoryController;
import com.easyadmin.module.goods.service.GoodsCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(GoodsCategoryController.class)
public class GoodsCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoodsCategoryService goodsCategoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/category/list"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        String requestBody = "{\"categoryName\":\"测试分类\",\"sort\":1,\"status\":1}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/goods/category/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInfo() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/goods/category/info/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDelete() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/goods/category/delete")
                .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
