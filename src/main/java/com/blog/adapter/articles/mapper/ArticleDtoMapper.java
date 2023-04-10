package com.blog.adapter.articles.mapper;

import com.blog.adapter.articles.dto.CreateArticleRequest;
import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleDtoMapper {
    ArticleDtoMapper MAPPER = Mappers.getMapper(ArticleDtoMapper.class);
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "html",target = "content")
    Article toModel(CreateArticleRequest articleRequest, String html, Integer wordNumbers, List<Tag> tags);






}
