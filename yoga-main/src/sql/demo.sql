-- 创建名为 scaffold 的数据库，指定字符集和排序规则（以 MySQL 为例）
CREATE DATABASE IF NOT EXISTS scaffold
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 切换到刚创建的数据库（可选，用于后续操作）
USE scaffold;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint(20) NOT NULL COMMENT 'id',
                         `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
                         `id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'GL', '123123');
