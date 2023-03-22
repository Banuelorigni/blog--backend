package com.example.blog.domain.articles;

import com.example.blog.infrastructure.articles.ArticlesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaArticleRepository extends JpaRepository<ArticlesEntity,Long> {
}
