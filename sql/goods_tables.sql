USE easyadmin;

-- 商品分类表
CREATE TABLE IF NOT EXISTS goods_category (
  id bigint primary key auto_increment comment '主键ID',
  category_name varchar(50) not null comment '分类名称',
  parent_id bigint default 0 comment '父分类ID',
  sort int default 0 comment '排序',
  status tinyint default 1 comment '1正常 0禁用',
  create_time datetime default current_timestamp comment '创建时间',
  update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
  remark varchar(255) character set utf8mb4 comment '备注'
) comment='商品分类表' character set utf8mb4;

-- 初始化商品分类数据
INSERT IGNORE INTO goods_category (category_name, parent_id, sort, status) VALUES 
('电子产品', 0, 1, 1),
('服装', 0, 2, 1),
('食品', 0, 3, 1);

-- 供应商表
CREATE TABLE IF NOT EXISTS supplier (
  id bigint primary key auto_increment comment '主键ID',
  supplier_name varchar(100) not null comment '供应商名称',
  contact_person varchar(50) comment '联系人',
  phone varchar(20) comment '联系电话',
  address varchar(255) character set utf8mb4 comment '地址',
  status tinyint default 1 comment '1正常 0禁用',
  create_time datetime default current_timestamp comment '创建时间',
  update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
  remark varchar(255) character set utf8mb4 comment '备注'
) comment='供应商表' character set utf8mb4;

-- 初始化供应商数据
INSERT IGNORE INTO supplier (supplier_name, contact_person, phone, address, status) VALUES 
('供应商A', '张三', '13800138001', '北京市朝阳区', 1),
('供应商B', '李四', '13800138002', '上海市浦东新区', 1);

-- 商品表
CREATE TABLE IF NOT EXISTS goods (
  id bigint primary key auto_increment comment '主键ID',
  goods_name varchar(100) not null comment '商品名称',
  category_id bigint comment '分类ID',
  goods_spec varchar(100) comment '商品规格',
  price decimal(10,2) not null comment '销售价格',
  cost_price decimal(10,2) not null comment '成本价格',
  stock int default 0 comment '库存数量',
  warn_stock int default 0 comment '预警库存',
  status tinyint default 1 comment '1正常 0禁用',
  create_time datetime default current_timestamp comment '创建时间',
  update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
  remark varchar(255) character set utf8mb4 comment '备注'
) comment='商品表' character set utf8mb4;

-- 初始化商品数据
INSERT IGNORE INTO goods (goods_name, category_id, goods_spec, price, cost_price, stock, warn_stock, status) VALUES 
('手机', 1, '128GB', 5999.00, 5000.00, 100, 10, 1),
('电脑', 1, '8GB/256GB', 4999.00, 4000.00, 50, 5, 1),
('T恤', 2, 'M', 99.00, 50.00, 200, 20, 1),
('牛仔裤', 2, '32', 199.00, 100.00, 150, 15, 1),
('苹果', 3, '500g', 9.90, 5.00, 500, 50, 1),
('香蕉', 3, '500g', 6.90, 3.00, 400, 40, 1);

-- 商品入库表
CREATE TABLE IF NOT EXISTS goods_in (
  id bigint primary key auto_increment comment '主键ID',
  goods_id bigint not null comment '商品ID',
  supplier_id bigint not null comment '供应商ID',
  in_num int not null comment '入库数量',
  in_price decimal(10,2) not null comment '入库单价',
  total_amount decimal(10,2) not null comment '总金额',
  operator varchar(50) comment '操作人',
  create_time datetime default current_timestamp comment '创建时间',
  remark varchar(255) character set utf8mb4 comment '备注'
) comment='商品入库表' character set utf8mb4;

-- 商品出库表
CREATE TABLE IF NOT EXISTS goods_out (
  id bigint primary key auto_increment comment '主键ID',
  goods_id bigint not null comment '商品ID',
  out_num int not null comment '出库数量',
  out_price decimal(10,2) not null comment '出库单价',
  total_amount decimal(10,2) not null comment '总金额',
  operator varchar(50) comment '操作人',
  create_time datetime default current_timestamp comment '创建时间',
  remark varchar(255) character set utf8mb4 comment '备注'
) comment='商品出库表' character set utf8mb4;

-- 商品记录表
CREATE TABLE IF NOT EXISTS goods_record (
  id bigint primary key auto_increment comment '主键ID',
  goods_id bigint not null comment '商品ID',
  record_type tinyint not null comment '记录类型 1入库 2出库',
  num int not null comment '数量',
  price decimal(10,2) not null comment '单价',
  total_amount decimal(10,2) not null comment '总金额',
  operator varchar(50) comment '操作人',
  create_time datetime default current_timestamp comment '创建时间',
  remark varchar(255) character set utf8mb4 comment '备注'
) comment='商品记录表' character set utf8mb4;