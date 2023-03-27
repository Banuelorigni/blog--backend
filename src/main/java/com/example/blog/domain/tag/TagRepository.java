package com.example.blog.domain.tag;

import com.example.blog.domain.articles.Article;
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
