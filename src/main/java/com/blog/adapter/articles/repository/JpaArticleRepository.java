package com.blog.adapter.articles.repository;

import com.blog.infrastructure.articles.ArticlesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaArticleRepository extends JpaRepository<ArticlesEntity,Long> {
}
