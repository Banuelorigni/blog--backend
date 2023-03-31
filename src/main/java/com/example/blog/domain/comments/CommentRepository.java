package com.example.blog.domain.comments;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface CommentRepository {
    Comment save(Comment comment);

    Page<Comment> getCommentByArticleId(Long articleId, String orderBy, String sortBy, int page, int size);
}
