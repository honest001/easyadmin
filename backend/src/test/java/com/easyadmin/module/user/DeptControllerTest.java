package com.easyadmin.module.user;

import com.easyadmin.module.user.entity.DeptEntity;
import com.easyadmin.module.user.controller.DeptController;
import com.easyadmin.module.user.service.DeptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(DeptController.class)
public class DeptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeptService deptService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/user/dept/list"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        String requestBody = "{\"deptName\":\"测试部门\",\"sort\":1,\"status\":1}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/user/dept/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInfo() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/user/dept/info/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDelete() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/user/dept/delete")
                .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
