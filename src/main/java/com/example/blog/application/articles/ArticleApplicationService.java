package com.example.blog.application.articles;

import com.example.blog.domain.articles.Article;
import com.example.blog.domain.articles.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleApplicationService {
    private ArticleRepository articleRepository;
    public Integer countWordNumber(String content) {
        return content.length();
    }

    public Article createArticles(Article article) {
        return articleRepository.save(article);
    }
}
