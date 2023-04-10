package com.blog.adapter.comments.repository;

import com.blog.domain.comments.Comment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public interface CommentRepository {
    Comment save(Comment comment);

    Page<Comment> getCommentByArticleId(Long articleId, String orderBy, String sortBy, int page, int size);
}
