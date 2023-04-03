package com.blog.domain.articles;

import com.blog.domain.articles.mapper.ArticleEntityMapper;
import com.blog.application.articles.exceptions.ArticleNotFoundException;
import com.blog.infrastructure.articles.ArticlesEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Override
    public Page<Article> getAllArticles(String orderBy, String sortBy, int page, int size) {
        Sort.Direction sortDirection = orderBy.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, sortDirection, sortBy);

        Page<ArticlesEntity> articlesEntities = jpaArticleRepository.findAll(pageRequest);
        if (articlesEntities.getTotalElements() == 0) {
            throw new ArticleNotFoundException("Article");
        }
        return articlesEntities.map(ArticleEntityMapper.MAPPER::toModel);

    }

    @Override
    public Article getArticleById(Long articleId) {
        ArticlesEntity articlesEntity = jpaArticleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("文章" + articleId));
        return ArticleEntityMapper.MAPPER.toModel(articlesEntity);
    }
}
