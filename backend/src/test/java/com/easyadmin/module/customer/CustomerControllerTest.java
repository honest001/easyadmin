package com.easyadmin.module.customer;

import com.easyadmin.module.customer.controller.CustomerController;
import com.easyadmin.module.customer.entity.CustomerEntity;
import com.easyadmin.module.customer.entity.CustomerRecordEntity;
import com.easyadmin.module.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/customer/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        String requestBody = "{\"customerName\":\"测试客户\",\"customerPhone\":\"13800138000\",\"customerAddress\":\"测试地址\"}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSaveRecord() throws Exception {
        // 构建测试数据
        String requestBody = "{\"customerId\":1,\"recordContent\":\"测试往来记录\"}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/record/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRecordList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/customer/record/list")
                .param("customerId", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInfo() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/customer/info/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateStatus() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/customer/updateStatus")
                .param("id", "1")
                .param("status", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
