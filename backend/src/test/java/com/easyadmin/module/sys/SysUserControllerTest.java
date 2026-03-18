package com.easyadmin.module.sys;

import com.easyadmin.module.sys.controller.SysUserController;
import com.easyadmin.module.sys.entity.SysUserEntity;
import com.easyadmin.module.sys.service.SysUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@WebMvcTest(SysUserController.class)
public class SysUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SysUserService sysUserService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testList() throws Exception {
        // 构建测试参数
        Map<String, Object> params = new HashMap<>();
        params.put("pageNum", 1);
        params.put("pageSize", 10);

        // 模拟service返回结果
        // PageResult<SysUserEntity> pageResult = new PageResult<>();
        // pageResult.setTotal(0L);
        // pageResult.setRecords(new ArrayList<>());
        // Mockito.when(sysUserService.pageList(params)).thenReturn(pageResult);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/sys/user/list")
                .param("pageNum", "1")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testSave() throws Exception {
        // 构建测试数据
        SysUserEntity user = new SysUserEntity();
        user.setUsername("test");
        user.setPassword("123456");
        user.setRealName("测试用户");

        // 模拟service方法
        // Mockito.when(sysUserService.saveOrUpdateUser(user)).thenReturn(true);

        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/sys/user/save")
                .contentType("application/json")
                .content("{\"username\":\"test\",\"password\":\"123456\",\"realName\":\"测试用户\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateStatus() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.post("/sys/user/updateStatus")
                .param("id", "1")
                .param("status", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testInfo() throws Exception {
        // 执行测试
        mockMvc.perform(MockMvcRequestBuilders.get("/sys/user/info/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
