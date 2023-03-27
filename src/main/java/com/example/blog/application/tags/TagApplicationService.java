package com.example.blog.application.tags;

import com.example.blog.domain.articles.Article;
import com.example.blog.domain.tag.Tag;
import com.example.blog.domain.tag.TagRepository;
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
