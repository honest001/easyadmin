package com.easyadmin.module.user;

import com.easyadmin.module.user.controller.EmployeeController;
import com.easyadmin.module.user.entity.EmployeeEntity;
import com.easyadmin.module.user.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/user/employee/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        String requestBody = "{\"empName\":\"测试员工\",\"empPhone\":\"13800138000\",\"deptId\":1}";

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/user/employee/save")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInfo() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/user/employee/info/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateStatus() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/user/employee/updateStatus")
                .param("id", "1")
                .param("status", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
