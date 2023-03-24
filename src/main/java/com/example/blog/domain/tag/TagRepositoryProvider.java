package com.example.blog.domain.tag;

import com.example.blog.domain.articles.Article;
import com.example.blog.domain.articles.mapper.ArticleEntityMapper;
import com.example.blog.domain.tag.mapper.TagEntityMapper;
import com.example.blog.infrastructure.tags.TagsEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TagRepositoryProvider implements TagRepository{
    private final JpaTagRepository jpaTagRepository;
    @Override
    public List<Article> findById(Long tagId) {
        TagsEntity tags = jpaTagRepository.findById(tagId).orElse(null);
        return ArticleEntityMapper.MAPPER.toListModel(tags.getArticles());
    }

    @Override
    public void save(List<String> tags) {
        List<Tag> tagList = tags.stream().map(tag -> Tag.builder().name(tag).build()).toList();
        List<TagsEntity> tagsEntities = tagList.stream().map(TagEntityMapper.MAPPER::toEntity).toList();

        jpaTagRepository.saveAll(tagsEntities);
    }

    @Override
    public List<Tag> findAllTagById(List<Long> tags) {
        List<TagsEntity> tagsEntities = jpaTagRepository.findAllById(tags);
        return tagsEntities.stream().map(TagEntityMapper.MAPPER::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Tag> findAll() {
        return TagEntityMapper.MAPPER.toEntity(jpaTagRepository.findAll());
    }
}
