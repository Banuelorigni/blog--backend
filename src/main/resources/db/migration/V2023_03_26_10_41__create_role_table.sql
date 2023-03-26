CREATE TABLE if not EXISTS `role`
(
    `id`                bigint(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role_name`              varchar(16) NOT NULL,
    `created_at`        datetime    NOT NULL,
    `updated_at`        datetime    NOT NULL,
    `created_by`        varchar(36) NOT NULL,
    `updated_by`        varchar(36) NOT NULL,
    `deleted`           tinyint(1) NOT NULL DEFAULT '0',
    `version`           int(11) NOT NULL
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;


