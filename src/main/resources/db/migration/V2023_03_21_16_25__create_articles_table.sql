CREATE TABLE if not EXISTS `articles`
(
    `id`           bigint(11)   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `title`        VARCHAR(255) NOT NULL,
    `content`      TEXT         NOT NULL,
    `word_numbers` INT(11)      NOT NULL,
    `cover_url`    VARCHAR(255) NOT NULL,
    `created_at`   datetime     ,
    `updated_at`   datetime      ,
    `created_by`   varchar(36)   ,
    `updated_by`   varchar(36)  ,
    `deleted`      tinyint(1)   NOT NULL DEFAULT '0',
    `version`      int(11)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
