package com.example.blog.domain.articles;

import org.springframework.stereotype.Component;

@Component
public interface ArticleRepository {
    Article save(Article article);
}
