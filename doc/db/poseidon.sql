CREATE DATABASE IF NOT EXISTS `poseidon` DEFAULT CHARACTER SET utf8 collate utf8_general_ci;
USE `poseidon`;

CREATE TABLE `poseidon_env` (
  `id` BIGINT AUTO_INCREMENT COMMENT '主键',
  `env_code` VARCHAR(100) NOT NULL COMMENT 'ENV',
  `env_name` VARCHAR(100) NOT NULL COMMENT '环境名称',
  `order` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '显示排序',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE 'uk_env_code'(`env_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '环境表';

CREATE TABLE `poseidon_project` (
   `id` BIGINT AUTO_INCREMENT COMMENT '主键',
   `project_code` VARCHAR(100) NOT NULL COMMENT '项目code',
   `project_name` VARCHAR(100) NOT NULL COMMENT '项目显示名称',
   `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
   `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
   PRIMARY KEY (`id`),
   UNIQUE 'uk_project_code'(`project_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '项目表';

CREATE TABLE `poseidon_user`(

);