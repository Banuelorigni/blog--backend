package com.blog.adapter.tags.repository;

import com.blog.domain.articles.Article;
import com.blog.infrastructure.articles.mapper.ArticleEntityMapper;
import com.blog.domain.tag.Tag;
import com.blog.infrastructure.tags.mapper.TagEntityMapper;
import com.blog.infrastructure.tags.TagsEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class TagRepositoryProvider implements TagRepository{
    private final JpaTagRepository jpaTagRepository;
    @Override
    public List<Article> findById(Long tagId) {
        TagsEntity tags = jpaTagRepository.findById(tagId).orElse(null);
        return ArticleEntityMapper.MAPPER.toListModel(Objects.requireNonNull(tags).getArticles());
    }

    @Override
    public Tag saveTag(String tag) {
        TagsEntity tagsEntity = TagEntityMapper.MAPPER.toEntity(tag);
        return TagEntityMapper.MAPPER.toModel(jpaTagRepository.save(tagsEntity));
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
