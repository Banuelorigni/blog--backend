package com.example.blog.domain.comments;

import com.example.blog.infrastructure.comments.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<CommentEntity,Long> {
}
