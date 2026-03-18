package com.easyadmin.common.constant;

/**
 * 系统通用常量类
 */
public class CommonConstant {
    /**
     * 超级管理员ID
     */
    public static final Long ADMIN_ID = 1L;
    
    /**
     * 超级管理员账号
     */
    public static final String ADMIN_USERNAME = "admin";
    
    /**
     * 密码加密盐值
     */
    public static final String PASSWORD_SALT = "easyadmin";
    
    /**
     * 成功状态码
     */
    public static final Integer SUCCESS_CODE = 200;
    
    /**
     * 失败状态码
     */
    public static final Integer FAIL_CODE = 500;
    
    /**
     * 正常状态
     */
    public static final Integer STATUS_NORMAL = 1;
    
    /**
     * 禁用状态
     */
    public static final Integer STATUS_DISABLE = 0;
    
    /**
     * 待审核状态
     */
    public static final Integer STATUS_PENDING = 0;
    
    /**
     * 通过状态
     */
    public static final Integer STATUS_APPROVED = 1;
    
    /**
     * 驳回状态
     */
    public static final Integer STATUS_REJECTED = 2;
    
    /**
     * 未收款状态
     */
    public static final Integer PAY_STATUS_UNPAID = 0;
    
    /**
     * 部分收款状态
     */
    public static final Integer PAY_STATUS_PARTIAL = 1;
    
    /**
     * 已收款状态
     */
    public static final Integer PAY_STATUS_PAID = 2;
    
    /**
     * 待处理订单状态
     */
    public static final Integer ORDER_STATUS_PENDING = 0;
    
    /**
     * 已完成订单状态
     */
    public static final Integer ORDER_STATUS_COMPLETED = 1;
    
    /**
     * 已取消订单状态
     */
    public static final Integer ORDER_STATUS_CANCELLED = 2;
    
    /**
     * 普通客户类型
     */
    public static final Integer CUSTOMER_TYPE_NORMAL = 1;
    
    /**
     * VIP客户类型
     */
    public static final Integer CUSTOMER_TYPE_VIP = 2;
    
    /**
     * 销售出库类型
     */
    public static final Integer OUT_TYPE_SALE = 1;
    
    /**
     * 损耗出库类型
     */
    public static final Integer OUT_TYPE_LOSS = 2;
    
    /**
     * 请假审批类型
     */
    public static final Integer APPROVE_TYPE_LEAVE = 1;
    
    /**
     * 报销审批类型
     */
    public static final Integer APPROVE_TYPE_REIMBURSE = 2;
    
    /**
     * 采购审批类型
     */
    public static final Integer APPROVE_TYPE_PURCHASE = 3;
    
    /**
     * 通用审批类型
     */
    public static final Integer APPROVE_TYPE_GENERAL = 4;
    
    /**
     * 现金支付方式
     */
    public static final Integer PAY_TYPE_CASH = 1;
    
    /**
     * 微信支付方式
     */
    public static final Integer PAY_TYPE_WECHAT = 2;
    
    /**
     * 支付宝支付方式
     */
    public static final Integer PAY_TYPE_ALIPAY = 3;
    
    /**
     * 转账支付方式
     */
    public static final Integer PAY_TYPE_TRANSFER = 4;
}