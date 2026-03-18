package com.easyadmin.module.report;

import com.easyadmin.module.report.controller.ReportController;
import com.easyadmin.module.report.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBusiness() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/report/business")
                .param("statType", "day")
                .param("statTime", "2026-03-17"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testStock() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/report/stock"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testAttendance() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/report/attendance")
                .param("salaryMonth", "2026-03"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testExport() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/report/export")
                .param("reportType", "business")
                .param("statTime", "2026-03-17"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
