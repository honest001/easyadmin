USE easyadmin;

-- 员工表
CREATE TABLE IF NOT EXISTS employee (
  id bigint primary key auto_increment comment '主键ID',
  employee_name varchar(50) not null comment '员工姓名',
  dept_id bigint comment '部门ID',
  position varchar(50) comment '职位',
  phone varchar(20) comment '联系电话',
  email varchar(100) comment '邮箱',
  entry_time date comment '入职时间',
  status tinyint default 1 comment '1正常 0离职',
  create_time datetime default current_timestamp comment '创建时间',
  update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
  remark varchar(255) comment '备注'
) comment='员工表';

-- 部门表
CREATE TABLE IF NOT EXISTS dept (
  id bigint primary key auto_increment comment '主键ID',
  dept_name varchar(50) not null comment '部门名称',
  parent_id bigint default 0 comment '父部门ID',
  sort int default 0 comment '排序',
  create_time datetime default current_timestamp comment '创建时间',
  update_time datetime default current_timestamp on update current_timestamp comment '更新时间',
  remark varchar(255) comment '备注'
) comment='部门表';

-- 考勤表
CREATE TABLE IF NOT EXISTS attendance (
  id bigint primary key auto_increment comment '主键ID',
  employee_id bigint not null comment '员工ID',
  attendance_date date not null comment '考勤日期',
  status tinyint comment '状态 1正常 2迟到 3早退 4缺勤 5请假',
  remark varchar(255) comment '备注',
  create_time datetime default current_timestamp comment '创建时间'
) comment='考勤表';

-- 工资表
CREATE TABLE IF NOT EXISTS salary (
  id bigint primary key auto_increment comment '主键ID',
  employee_id bigint not null comment '员工ID',
  salary_month varchar(7) not null comment '工资月份',
  basic_salary decimal(10,2) not null comment '基本工资',
  bonus decimal(10,2) default 0 comment '奖金',
  deduction decimal(10,2) default 0 comment '扣除',
  real_salary decimal(10,2) not null comment '实发工资',
  remark varchar(255) comment '备注',
  create_time datetime default current_timestamp comment '创建时间',
  update_time datetime default current_timestamp on update current_timestamp comment '更新时间'
) comment='工资表';

-- 初始化部门数据
INSERT IGNORE INTO dept (dept_name, parent_id, sort) VALUES 
('总经办', 0, 1),
('技术部', 0, 2),
('销售部', 0, 3),
('财务部', 0, 4);

-- 初始化员工数据
INSERT IGNORE INTO employee (employee_name, dept_id, position, phone, email, entry_time, status) VALUES 
('张三', 1, '总经理', '13800138001', 'zhangsan@example.com', '2026-01-01', 1),
('李四', 2, '技术总监', '13800138002', 'lisi@example.com', '2026-01-01', 1),
('王五', 3, '销售经理', '13800138003', 'wangwu@example.com', '2026-01-01', 1),
('赵六', 4, '财务经理', '13800138004', 'zhaoliu@example.com', '2026-01-01', 1);

-- 初始化考勤数据
INSERT IGNORE INTO attendance (employee_id, attendance_date, status) VALUES 
(1, '2026-03-01', 1),
(1, '2026-03-02', 1),
(1, '2026-03-03', 1),
(2, '2026-03-01', 1),
(2, '2026-03-02', 2),
(2, '2026-03-03', 1),
(3, '2026-03-01', 1),
(3, '2026-03-02', 1),
(3, '2026-03-03', 1),
(4, '2026-03-01', 1),
(4, '2026-03-02', 1),
(4, '2026-03-03', 1);

-- 初始化工资数据
INSERT IGNORE INTO salary (employee_id, salary_month, basic_salary, bonus, deduction, real_salary) VALUES 
(1, '2026-03', 10000.00, 5000.00, 0.00, 15000.00),
(2, '2026-03', 8000.00, 3000.00, 0.00, 11000.00),
(3, '2026-03', 6000.00, 4000.00, 0.00, 10000.00),
(4, '2026-03', 7000.00, 2000.00, 0.00, 9000.00);