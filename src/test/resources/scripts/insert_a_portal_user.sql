insert into `role` (`id`, `role_name`, `created_at`, `updated_at`, `created_by`, `updated_by`, `deleted`, `version`)
values (2, 'PORTAL_USER', '2022-10-12 13:19:53', '2022-10-12 13:19:53', 'admin', 'admin', 0, 1);

ALTER TABLE `users` ALTER COLUMN id RESTART WITH 1;

INSERT INTO `users` (`id`, `role_id`, `nickname`, `username`, `password`, `created_at`, `updated_at`, `created_by`,
                     `updated_by`, `deleted`, `version`)
VALUES (2, 2, 'portal_user', 'portal_user', '$2a$10$8D4p7tQUJG.gk5nBJvKIDe.j/EohJ.dcfxMzCkMCCArSLHK0Ud5B.',
        '2022-10-12 13:19:53',
        '2022-10-12 13:19:53', 'admin', 'admin', 0, 1);
