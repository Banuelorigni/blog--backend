package com.blog.adapter.user.repository;


import com.blog.BlogApplication;
import com.blog.domain.user.Role;
import com.blog.domain.user.User;
import com.blog.infrastructure.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = BlogApplication.class)
@ActiveProfiles("test")
@Transactional
class UserRepositoryProviderTest {
    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private UserRepositoryProvider userRepositoryProvider;

    @Test
    void shouldReturnFalseWhenNotExists() {
        assertFalse(userRepositoryProvider.exist("username"));
    }

    @Test
    @Sql("classpath:scripts/insert_an_admin_user.sql")
    void shouldReturnTrueWhenExists() {
        assertTrue(userRepositoryProvider.exist("libingbing"));
    }

    @Test
    void shouldReturnNullWhenFindByIdButNone() {
        Optional<UserEntity> userEntity = userRepositoryProvider.findById(1L);

        assertFalse(userEntity.isPresent());
    }

    @Test
    @Sql("classpath:scripts/insert_admin_role.sql")
    void shouldSaveUserCorrectly() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("encryptedPassword");
        user.setRole(Role.builder().roleName("ADMIN").id(1L).build());
        user.setId(null);

        User savedUser = userRepositoryProvider.save(user);

        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertNotNull(savedUser.getId());
        assertEquals(user.getRole(), savedUser.getRole());

        List<UserEntity> userEntities = jpaUserRepository.findAll();
        assertEquals(1, userEntities.size());
        UserEntity userEntity = userEntities.get(0);
        assertEquals(user.getUsername(), userEntity.getUsername());
        assertEquals(user.getPassword(), userEntity.getPassword());
        assertNotNull(userEntity.getId());
        assertEquals(1L, userEntity.getRole().getId());
        assertFalse(userEntity.isDeleted());
    }

    @Test
    @Sql("classpath:scripts/insert_an_admin_user.sql")
    void shouldReturnUserEntityWhenFindById() {
        UserEntity userEntity = userRepositoryProvider.findById(1L).orElseThrow();

        assertEquals(1L, userEntity.getId());
        assertEquals("libingbing", userEntity.getUsername());
        assertEquals("libingbing", userEntity.getNickname());
        assertEquals(1, userEntity.getRole().getId());
        assertEquals("$2a$10$8D4p7tQUJG.gk5nBJvKIDe.j/EohJ.dcfxMzCkMCCArSLHK0Ud5B.", userEntity.getPassword());
        assertFalse(userEntity.getDeleted());
    }

    @Test
    void shouldReturnNullWhenFindByUsernameButNone() {
        Optional<UserEntity> userEntity = userRepositoryProvider.findByUsername("libingbing");

        assertFalse(userEntity.isPresent());
    }

    @Test
    @Sql("classpath:scripts/insert_an_admin_user.sql")
    void shouldReturnUserEntityWhenFindByUsername() {
        UserEntity userEntity = userRepositoryProvider.findByUsername("libingbing").orElseThrow();

        assertEquals(1L, userEntity.getId());
        assertEquals("libingbing", userEntity.getUsername());
        assertEquals("libingbing", userEntity.getNickname());
        assertEquals(1, userEntity.getRole().getId());
        assertEquals("$2a$10$8D4p7tQUJG.gk5nBJvKIDe.j/EohJ.dcfxMzCkMCCArSLHK0Ud5B.", userEntity.getPassword());
        assertFalse(userEntity.getDeleted());
    }

    @Test
    @Sql("classpath:scripts/insert_a_portal_user.sql")
    void findAll() {
        Pageable pageable = PageRequest.of(0, 15, Sort.Direction.DESC, "createdAt");
        Page<User> users = userRepositoryProvider.findAll(pageable);

        assertEquals(1, users.getTotalElements());
    }
}
