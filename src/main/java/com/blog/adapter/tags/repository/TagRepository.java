package com.blog.adapter.tags.repository;

import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TagRepository {

    void save(List<String> tags);

    List<Tag> findAllTagById(List<Long> tags);

    List<Tag> findAll();

    List<Article> findById(Long tagId);

    Tag saveTag(String tag);
}
