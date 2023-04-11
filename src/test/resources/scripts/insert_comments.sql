INSERT INTO `comments`
( content, user_id, article_id,
 created_at, updated_at, created_by, updated_by, deleted, version)
VALUES ( 'test', 2, 1, now(), now(), '', '', 0, 1);
