package com.example.blog.domain.articles.mapper;

import com.example.blog.domain.articles.Article;
import com.example.blog.infrastructure.articles.ArticlesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleEntityMapper {
    ArticleEntityMapper MAPPER = Mappers.getMapper(ArticleEntityMapper.class);

    ArticlesEntity toEntity(Article article);

    Article toModel(ArticlesEntity articlesEntity);


}
