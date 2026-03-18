USE easyadmin;

-- 订单主表
CREATE TABLE IF NOT EXISTS order_info (
  id bigint primary key auto_increment comment '主键ID',
  order_no varchar(50) not null unique comment '订单编号',
  customer_id bigint comment '客户ID',
  total_amount decimal(10,2) not null comment '总金额',
  order_status tinyint default 0 comment '订单状态 0待处理 1已完成 2已取消',
  pay_status tinyint default 0 comment '收款状态 0未收款 1部分收款 2已收款',
  order_time datetime comment '订单时间',
  operator bigint comment '操作人ID',
  remark varchar(255) comment '备注',
  create_time datetime default current_timestamp comment '创建时间',
  update_time datetime default current_timestamp on update current_timestamp comment '更新时间'
) comment='订单主表';

-- 插入测试数据
INSERT INTO order_info (order_no, customer_id, total_amount, order_status, pay_status, order_time, operator, remark) VALUES
('ORD20260301001', 1, 1000.00, 1, 2, '2026-03-01 10:00:00', 1, '测试订单1'),
('ORD20260302002', 2, 2000.00, 1, 2, '2026-03-02 11:00:00', 1, '测试订单2'),
('ORD20260303003', 3, 1500.00, 1, 2, '2026-03-03 14:00:00', 1, '测试订单3');

-- 订单明细表
CREATE TABLE IF NOT EXISTS order_item (
  id bigint primary key auto_increment comment '主键ID',
  order_id bigint not null comment '订单ID',
  goods_id bigint not null comment '商品ID',
  goods_name varchar(100) not null comment '商品名称',
  goods_spec varchar(100) comment '商品规格',
  goods_price decimal(10,2) not null comment '商品单价',
  goods_num int not null comment '商品数量',
  item_amount decimal(10,2) not null comment '项目金额',
  create_time datetime default current_timestamp comment '创建时间'
) comment='订单明细表';

-- 订单收款表
CREATE TABLE IF NOT EXISTS order_pay (
  id bigint primary key auto_increment comment '主键ID',
  order_id bigint not null comment '订单ID',
  pay_amount decimal(10,2) not null comment '收款金额',
  pay_time datetime comment '收款时间',
  pay_type varchar(20) comment '收款方式',
  operator varchar(50) comment '操作人',
  remark varchar(255) comment '备注',
  create_time datetime default current_timestamp comment '创建时间'
) comment='订单收款表';