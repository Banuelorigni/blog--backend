package com.blog.domain.comments;

import com.blog.infrastructure.comments.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<CommentEntity,Long> {
    Page<CommentEntity> findAllByArticleId(Long articleId, PageRequest pageRequest);
}
