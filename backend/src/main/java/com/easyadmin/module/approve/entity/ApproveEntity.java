package com.easyadmin.module.approve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 审批申请实体类（对应approve表）
 */
@TableName("approve")
public class ApproveEntity {
    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 审批单号
     */
    private String approveNo;

    /**
     * 申请人ID
     */
    private Long applyEmpId;

    /**
     * 审批类型：1请假 2报销 3采购 4通用
     */
    private Integer approveType;

    /**
     * 审批标题
     */
    private String approveTitle;

    /**
     * 审批内容
     */
    private String approveContent;

    /**
     * 申请时间
     */
    private LocalDateTime applyTime;

    /**
     * 审批人ID
     */
    private Long approveUser;

    /**
     * 审批时间
     */
    private LocalDateTime approveTime;

    /**
     * 审批状态：0待审核 1通过 2驳回
     */
    private Integer approveStatus;

    /**
     * 审批意见
     */
    private String approveOpinion;

    /**
     * 附件路径
     */
    private String filePath;

    /**
     * 备注
     */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApproveNo() {
        return approveNo;
    }

    public void setApproveNo(String approveNo) {
        this.approveNo = approveNo;
    }

    public Long getApplyEmpId() {
        return applyEmpId;
    }

    public void setApplyEmpId(Long applyEmpId) {
        this.applyEmpId = applyEmpId;
    }

    public Integer getApproveType() {
        return approveType;
    }

    public void setApproveType(Integer approveType) {
        this.approveType = approveType;
    }

    public String getApproveTitle() {
        return approveTitle;
    }

    public void setApproveTitle(String approveTitle) {
        this.approveTitle = approveTitle;
    }

    public String getApproveContent() {
        return approveContent;
    }

    public void setApproveContent(String approveContent) {
        this.approveContent = approveContent;
    }

    public LocalDateTime getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(LocalDateTime applyTime) {
        this.applyTime = applyTime;
    }

    public Long getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(Long approveUser) {
        this.approveUser = approveUser;
    }

    public LocalDateTime getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(LocalDateTime approveTime) {
        this.approveTime = approveTime;
    }

    public Integer getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Integer approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getApproveOpinion() {
        return approveOpinion;
    }

    public void setApproveOpinion(String approveOpinion) {
        this.approveOpinion = approveOpinion;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}