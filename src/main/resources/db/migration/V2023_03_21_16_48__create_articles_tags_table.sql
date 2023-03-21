CREATE TABLE if not EXISTS `articles_tags`
(
    `id`         bigint(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `article_id` bigint(11)  NOT NULL,
    `tag_id`     bigint(11)  NOT NULL,

    `created_at` datetime    NOT NULL,
    `updated_at` datetime    NOT NULL,
    `created_by` varchar(36) NOT NULL,
    `updated_by` varchar(36) NOT NULL,
    `deleted`    tinyint(1)  NOT NULL DEFAULT '0',
    `version`    int(11)     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
