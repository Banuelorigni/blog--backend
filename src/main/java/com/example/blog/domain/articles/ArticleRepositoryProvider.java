package com.example.blog.domain.articles;

import com.example.blog.domain.articles.mapper.ArticleEntityMapper;
import com.example.blog.infrastructure.articles.ArticlesEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ArticleRepositoryProvider implements ArticleRepository {
    private final JpaArticleRepository jpaArticleRepository;

    @Override
    public Article save(Article article) {
        ArticlesEntity articlesEntity = jpaArticleRepository.save(ArticleEntityMapper.MAPPER.toEntity(article));
        return ArticleEntityMapper.MAPPER.toModel(articlesEntity);
    }
}
