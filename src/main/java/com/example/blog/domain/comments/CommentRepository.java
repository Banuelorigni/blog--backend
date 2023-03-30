package com.example.blog.domain.comments;

import org.springframework.stereotype.Component;

@Component
public interface CommentRepository {
    Comment save(Comment comment);
}
