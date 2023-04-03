package com.blog.infrastructure.user;

import com.blog.domain.user.User;
import com.blog.domain.user.UserRepository;
import com.blog.infrastructure.user.entity.RoleEntity;
import com.blog.infrastructure.user.mapper.UserEntityMapper;
import com.blog.adapter.user.mapper.UserDtoMapper;
import com.blog.infrastructure.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserRepositoryProvider implements UserRepository {

    private JpaUserRepository repository;
    @Override
    public boolean exist(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        UserEntity saved = repository.save(UserEntityMapper.MAPPER.toEntity(user));

        return UserEntityMapper.MAPPER.toModel(saved);
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAllByRole(pageable, RoleEntity.builder().id(2L).build())
                .map(UserDtoMapper.MAPPER::toUser);
    }
}
