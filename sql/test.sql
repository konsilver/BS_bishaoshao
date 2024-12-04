

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

CREATE TABLE type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    parent_type VARCHAR(127),
    child_type VARCHAR(127)
    
);

CREATE TABLE card (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2),
    shop VARCHAR(100),
    img_url TEXT,
    source VARCHAR(8),
    child_type VARCHAR(127),
    thing_url TEXT,
    crawl_date DATETIME
    UNIQUE (title, shop, source, thing_url, child_type)
);

CREATE TABLE thing (
    id INT  PRIMARY KEY,
    params JSON
);

CREATE TABLE history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    thing_id INT,
    crawl_date DATETIME,
    price DECIMAL(10, 2)
);

CREATE TABLE subscribe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    thing_id INT,
    user_id INT
);

