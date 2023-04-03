package com.blog.adapter.comments;

import com.blog.adapter.comments.dto.request.CreateCommentRequest;
import com.blog.domain.comments.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentDtoMapper {
    CommentDtoMapper MAPPER = Mappers.getMapper(CommentDtoMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "commentRequest.article_id",target = "article_id")
    Comment toModel(CreateCommentRequest commentRequest, String userName);

}
