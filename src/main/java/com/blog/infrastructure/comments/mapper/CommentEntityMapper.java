package com.blog.infrastructure.comments.mapper;

import com.blog.domain.comments.Comment;
import com.blog.infrastructure.articles.ArticlesEntity;
import com.blog.infrastructure.comments.CommentEntity;
import com.blog.infrastructure.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentEntityMapper {
    CommentEntityMapper MAPPER = Mappers.getMapper(CommentEntityMapper.class);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "content",source = "comment.content")
    CommentEntity toEntity(Comment comment, ArticlesEntity article, UserEntity user);

    @Mapping(target = "userName", source = "user" ,qualifiedByName = "userEntityToStringConverter")
    @Mapping(target = "article_id",source = "article",qualifiedByName = "articleToLongConverter")
    Comment toModel(CommentEntity commentEntity);

    @Named("userEntityToStringConverter")
    default String userEntityToStringConverter(UserEntity userEntity) {
        return userEntity.getNickname();
    }

    @Named("articleToLongConverter")
    default Long articleToLongConverter(ArticlesEntity article){
        return article.getId();
    }
}
