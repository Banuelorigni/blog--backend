package com.example.blog.adapter.articles;

import com.example.blog.adapter.articles.dto.CreateArticleRequest;
import com.example.blog.domain.articles.Article;
import com.example.blog.domain.tag.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleDtoMapper {
    ArticleDtoMapper MAPPER = Mappers.getMapper(ArticleDtoMapper.class);
    @Mapping(source = "tags", target = "tags")
    Article toModel(CreateArticleRequest articleRequest, Integer wordNumbers, List<Tag> tags);






}
