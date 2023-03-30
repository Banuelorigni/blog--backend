package com.example.blog.domain.comments;

import com.example.blog.infrastructure.comments.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findAllByArticleId(Long articleId);
}
