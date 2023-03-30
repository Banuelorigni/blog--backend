package com.example.blog.domain.comments;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommentRepository {
    Comment save(Comment comment);

    List<Comment> getCommentByArticleId(Long articleId);
}
