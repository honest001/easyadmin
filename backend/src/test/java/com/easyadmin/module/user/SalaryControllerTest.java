package com.easyadmin.module.user;

import com.easyadmin.module.user.controller.SalaryController;
import com.easyadmin.module.user.service.SalaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(SalaryController.class)
public class SalaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalaryService salaryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testStat() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/user/salary/stat")
                .param("salaryMonth", "2026-03"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
