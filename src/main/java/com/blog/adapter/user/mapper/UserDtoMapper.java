package com.blog.adapter.user.mapper;

import com.blog.adapter.user.dto.request.UserRegisterRequest;
import com.blog.adapter.user.dto.response.UserInfoResponse;
import com.blog.adapter.user.dto.response.UserResponse;
import com.blog.domain.user.User;
import com.blog.infrastructure.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDtoMapper {

    UserDtoMapper MAPPER = Mappers.getMapper(UserDtoMapper.class);

    User toModel(UserRegisterRequest request);

    UserResponse toResponse(UserEntity userEntity);

    UserInfoResponse toUserInfoResponse(User user);

    User toUser(UserEntity userEntity);
}
