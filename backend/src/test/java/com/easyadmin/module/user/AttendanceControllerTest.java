package com.easyadmin.module.user;

import com.easyadmin.module.user.controller.AttendanceController;
import com.easyadmin.module.user.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AttendanceController.class)
public class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttendanceService attendanceService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheck() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/user/attendance/check"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testStat() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/user/attendance/stat")
                .param("salaryMonth", "2026-03"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
