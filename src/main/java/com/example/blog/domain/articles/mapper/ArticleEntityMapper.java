package com.example.blog.domain.articles.mapper;

import com.example.blog.domain.articles.Article;
import com.example.blog.infrastructure.articles.ArticlesEntity;
import com.example.blog.infrastructure.tags.TagsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArticleEntityMapper {
    ArticleEntityMapper MAPPER = Mappers.getMapper(ArticleEntityMapper.class);

//    @Mapping(source = "tags", target = "tags", qualifiedByName = "longToTagEntityConverter")
    ArticlesEntity toEntity(Article article);
    Article toModel(ArticlesEntity articlesEntity);

    @Named("longToTagEntityConverter")
    default TagsEntity longToTagEntityConverter(String tag) {
        TagsEntity entity = new TagsEntity();
        entity.setName(tag);
        return entity;
    }

    @Named("tagEntityToString")
    default String tagEntityToString(TagsEntity tag) {
        return tag.getName();
    }

}
