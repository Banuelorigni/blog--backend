package com.example.blog.infrastructure.user;

import com.example.blog.infrastructure.user.entity.RoleEntity;
import com.example.blog.infrastructure.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);

    Optional<UserEntity> findByUsername(String username);

    Page<UserEntity> findAllByRole(Pageable pageable, RoleEntity role);

}
