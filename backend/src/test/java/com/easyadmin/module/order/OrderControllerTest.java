package com.easyadmin.module.order;

import com.easyadmin.module.order.entity.OrderInfoEntity;
import com.easyadmin.module.order.entity.OrderItemEntity;
import com.easyadmin.module.order.entity.OrderPayEntity;
import com.easyadmin.module.order.controller.OrderController;
import com.easyadmin.module.order.service.OrderInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderInfoService orderInfoService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/order/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        String requestBody = "{\"order\":{\"customerId\":1},\"items\":[{\"goodsId\":1,\"goodsNum\":1,\"goodsPrice\":100}]}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/order/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPay() throws Exception {
        // 构建测试数据
        String requestBody = "{\"orderId\":1,\"payAmount\":100,\"payType\":1}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/order/pay")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCancel() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/order/cancel")
                .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPayList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/order/pay/list")
                .param("orderId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
