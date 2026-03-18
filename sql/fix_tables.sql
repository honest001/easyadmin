USE easyadmin;

-- 创建sys_oper_log表（如果不存在）
CREATE TABLE IF NOT EXISTS sys_oper_log (
  id bigint primary key auto_increment comment '主键ID',
  title varchar(50) comment '操作标题',
  business_type tinyint comment '业务类型',
  method varchar(100) comment '请求方法',
  request_uri varchar(255) comment '请求URI',
  oper_ip varchar(50) comment '操作IP',
  oper_user varchar(50) comment '操作人',
  oper_time datetime default current_timestamp comment '操作时间',
  status tinyint default 1 comment '1正常 0异常',
  error_msg varchar(500) comment '错误信息',
  remark varchar(255) comment '备注'
) comment='操作日志表';
