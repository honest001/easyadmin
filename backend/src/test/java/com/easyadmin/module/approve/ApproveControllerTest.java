package com.easyadmin.module.approve;

import com.easyadmin.module.approve.controller.ApproveController;
import com.easyadmin.module.approve.entity.ApproveEntity;
import com.easyadmin.module.approve.service.ApproveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ApproveController.class)
public class ApproveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApproveService approveService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testApply() throws Exception {
        // 构建测试数据
        String requestBody = "{\"approveType\":1,\"approveTitle\":\"测试审批\",\"approveContent\":\"测试内容\",\"approveUser\":1}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/approve/apply")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testWaitList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/approve/wait/list"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testOperate() throws Exception {
        // 构建测试数据
        String requestBody = "{\"id\":1,\"approveStatus\":1,\"approveOpinion\":\"同意\"}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/approve/operate")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testMyList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/approve/my/list"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFinishList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/approve/finish/list"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
