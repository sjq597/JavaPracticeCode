DROP DATABASE IF EXISTS sampledb;
CREATE DATABASE sampledb DEFAULT CHARACTER SET utf8mb4;
USE sampledb;

-- 创建用户表
CREATE TABLE t_user (
  user_id INT AUTO_INCREMENT NOT NULL COMMENT '用户id',
  user_name VARCHAR(30) NOT NULL DEFAULT '' COMMENT '用户名',
  credits INT NOT NULL DEFAULT 0 COMMENT '论坛积分',
  password VARCHAR(32) NOT NULL DEFAULT ''COMMENT '用户密码',
  last_visit TIMESTAMP NOT NULL DEFAULT 0 COMMENT '最后访问时间',
  last_ip VARCHAR(23) NOT NULL DEFAULT '0.0.0.0' COMMENT '最后访问ip',
  PRIMARY KEY (user_id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '用户表';

-- 创建用户登陆日志表
CREATE TABLE t_login_log (
  login_log_id INT AUTO_INCREMENT NOT NULL COMMENT '日志id',
  user_id INT NOT NULL DEFAULT 0 COMMENT '用户id',
  ip VARCHAR(23) NOT NULL DEFAULT '0.0.0.0' COMMENT '访问ip',
  login_datetime TIMESTAMP NOT NULL DEFAULT 0 COMMENT '访问时间',
  PRIMARY KEY (login_log_id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '用户登陆日志表';

INSERT INTO t_user (user_name,password) VALUES ('admin','123456');
SELECT * FROM t_user;