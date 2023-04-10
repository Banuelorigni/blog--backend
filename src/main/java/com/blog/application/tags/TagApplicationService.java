package com.blog.application.tags;

import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import com.blog.adapter.tags.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagApplicationService {
    private final TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public List<Article> findArticlesByTagId(Long tagId) {
        return tagRepository.findById(tagId);
    }

    public Tag createTag(String tag) {
        return tagRepository.saveTag(tag);
    }
}
