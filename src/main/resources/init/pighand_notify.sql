SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message_content
-- ----------------------------
DROP TABLE IF EXISTS `message_content`;
CREATE TABLE `message_content`
(
    `id`              bigint unsigned                                       NOT NULL AUTO_INCREMENT,
    `project_id`      bigint unsigned                                       NOT NULL,
    `scene`           varchar(16)                                           NOT NULL COMMENT '适用场景',
    `title`           varchar(32) DEFAULT NULL COMMENT '标题',
    `template`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板',
    `def_sender_type` tinyint     DEFAULT NULL COMMENT '默认发送类型',
    `def_sender_id`   bigint      DEFAULT NULL COMMENT '默认发送者id',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1632590527713034243
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='内容消息';

-- ----------------------------
-- Table structure for message_sms
-- ----------------------------
DROP TABLE IF EXISTS `message_sms`;
CREATE TABLE `message_sms`
(
    `id`             bigint unsigned                                              NOT NULL AUTO_INCREMENT,
    `project_id`     bigint                                                       NOT NULL,
    `cloud_platform` tinyint(1)                                                   NOT NULL COMMENT '云平台：1阿里云 2腾讯云',
    `appid`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `secret`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `scene`          varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '适用场景',
    `sign_name`      varchar(32)                                                  NOT NULL COMMENT '短信签名名称',
    `template_code`  varchar(32)                                                  NOT NULL COMMENT '短信模板code',
    `template_param` json                                                         NOT NULL COMMENT '短信模板参数',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='短信消息';

-- ----------------------------
-- Table structure for sender_email
-- ----------------------------
DROP TABLE IF EXISTS `sender_email`;
CREATE TABLE `sender_email`
(
    `id`         bigint unsigned NOT NULL AUTO_INCREMENT,
    `project_id` bigint unsigned NOT NULL,
    `account`    varchar(32)     NOT NULL COMMENT '邮箱账号',
    `name`       varchar(16)                                              DEFAULT NULL COMMENT '发送者名称',
    `password`   varchar(64)                                              DEFAULT NULL COMMENT '邮箱密码',
    `protocol`   char(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT 'smtp' COMMENT '发送协议：smtp、pop3、imap',
    `host`       varchar(32)     NOT NULL COMMENT '协议host',
    `port`       int unsigned                                             DEFAULT '465' COMMENT '端口',
    `ssl`        tinyint(1)                                               DEFAULT '1' COMMENT '启用SSL',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1632951401233620995
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='发送邮箱配置';

SET FOREIGN_KEY_CHECKS = 1;
