CREATE TABLE if not EXISTS `users`
(
    `id`         bigint(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `role_id`    bigint(11)  NOT NULL,
    `nickname`   varchar(36) NOT NULL,
    `username`   varchar(16) NOT NULL UNIQUE,
    `password`   varchar(60) NOT NULL,
    `avatar`     varchar(255)         DEFAULT NULL,
    `email`      varchar(50)          DEFAULT NULL,
    `mobile`     varchar(50)          DEFAULT NULL,

    `created_at` datetime    NOT NULL,
    `updated_at` datetime    NOT NULL,
    `created_by` varchar(36) NOT NULL,
    `updated_by` varchar(36) NOT NULL,
    `deleted`    tinyint(1)  NOT NULL DEFAULT '0',
    `version`    int(11)     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
