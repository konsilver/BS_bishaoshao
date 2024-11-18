

CREATE DATABASE IF NOT EXISTS bishaoshao;

use bishaoshao;
-- 用户的基本信息的表 
CREATE TABLE `user`
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(25) UNIQUE NOT NULL COMMENT '用户名', 
    password VARCHAR(64) NOT NULL COMMENT '密码',
    email VARCHAR(50) UNIQUE NOT NULL COMMENT '邮箱',
    PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (username, password, email) 
VALUES ('bishaoshao_tester', '931145d4ddd1811be545e4ac88a81f1fdbfaf0779c437efba16b884595274d11', '2139752165@qq.com');

