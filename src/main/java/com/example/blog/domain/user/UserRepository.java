package com.example.blog.domain.user;

import com.example.blog.infrastructure.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository {

    boolean exist(String username);

    User save(User user);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByUsername(String username);

    Page<User> findAll(Pageable pageable);
}
