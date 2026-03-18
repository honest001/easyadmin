package com.easyadmin.module.user.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.user.entity.DeptEntity;
import com.easyadmin.module.user.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门Controller
 */
@RestController
@RequestMapping("/user/dept")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 部门列表查询
     */
    @GetMapping("/list")
    public Result<List<DeptEntity>> list() {
        List<DeptEntity> deptList = deptService.listAll();
        return Result.success(deptList);
    }

    /**
     * 部门新增/编辑/删除
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody DeptEntity dept) {
        deptService.saveOrUpdateDept(dept);
        return Result.success();
    }

    /**
     * 部门删除
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        deptService.deleteDept(id);
        return Result.success();
    }
}