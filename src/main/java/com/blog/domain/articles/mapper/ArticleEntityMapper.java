package com.blog.domain.articles.mapper;

import com.blog.domain.articles.Article;
import com.blog.infrastructure.articles.ArticlesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleEntityMapper {
    ArticleEntityMapper MAPPER = Mappers.getMapper(ArticleEntityMapper.class);

    ArticlesEntity toEntity(Article article);

    Article toModel(ArticlesEntity articlesEntity);

    List<Article> toListModel(Page<ArticlesEntity> articlesEntities);

    List<Article> toListModel(List<ArticlesEntity> articles);
}
