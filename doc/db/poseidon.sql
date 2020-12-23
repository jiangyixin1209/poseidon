CREATE DATABASE IF NOT EXISTS `poseidon` DEFAULT CHARACTER SET utf8 collate utf8_general_ci;
USE `poseidon`;

CREATE TABLE `poseidon_env` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` VARCHAR(100) NOT NULL COMMENT 'ENV',
  `name` VARCHAR(100) NOT NULL COMMENT '环境名称',
  `sort` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '显示排序',
  `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
  `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE uk_env_code(`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '环境表';

CREATE TABLE `poseidon_project` (
   `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
   `code` VARCHAR(100) NOT NULL COMMENT '项目code',
   `name` VARCHAR(100) NOT NULL COMMENT '项目显示名称',
   `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
   `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
   PRIMARY KEY (`id`),
   UNIQUE uk_project_code(`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '项目表';

CREATE TABLE `poseidon_user`(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` CHAR(32) NOT NULL COMMENT '密码',
    `permission` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '权限 0-管理员，1-普通用户',
    `permission_data` TEXT DEFAULT NULL COMMENT '权限配置数据',
    `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE uk_username(`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '用户表';

CREATE TABLE `poseidon_config`(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `env_code` VARCHAR(100) NOT NULL COMMENT '配置环境code',
    `project_code` VARCHAR(100) NOT NULL COMMENT '所属项目code',
    `key` VARCHAR(100) NOT NULL COMMENT '配置key',
    `desc` VARCHAR(100) NOT NULL COMMENT '配置描述',
    `value` VARCHAR(2000) NOT NULL COMMENT '配置值',
    `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
    `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE uk_env_project_key(`env_code`, `project_code`, `key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '配置表';

CREATE TABLE `poseidon_config_log`(
      `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
      `env_code` VARCHAR(100) NOT NULL COMMENT '配置环境code',
      `project_code` VARCHAR(100) NOT NULL COMMENT '所属项目code',
      `key` VARCHAR(100) NOT NULL COMMENT '配置key',
      `desc` VARCHAR(100) NOT NULL COMMENT '变更描述',
      `old_value` VARCHAR(2000) NOT NULL COMMENT '旧值',
      `new_value` VARCHAR(2000) NOT NULL COMMENT '新值',
      `opt_user` VARCHAR(50) NOT NULL COMMENT '变更人',
      `gmt_create` DATETIME NOT NULL COMMENT '创建时间',
      `gmt_modified` DATETIME NOT NULL COMMENT '修改时间',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '配置变更日志表';

CREATE TABLE `poseidon_config_notify`(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '配置变更通知表';