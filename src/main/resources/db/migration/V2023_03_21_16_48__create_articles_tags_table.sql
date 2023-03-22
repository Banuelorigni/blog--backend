CREATE TABLE if not EXISTS `articles_tags`
(
    `id`         bigint(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `article_id` bigint(11)  NOT NULL,
    `tag_id`     bigint(11)  NOT NULL,

    `created_at`   datetime     ,
    `updated_at`   datetime      ,
    `created_by`   varchar(36)   ,
    `updated_by`   varchar(36)  ,
    `deleted`      tinyint(1)   NOT NULL DEFAULT '0',
    `version`      int(11)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
