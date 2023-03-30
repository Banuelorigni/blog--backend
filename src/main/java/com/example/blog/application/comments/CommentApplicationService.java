package com.example.blog.application.comments;

import com.example.blog.domain.comments.Comment;
import com.example.blog.domain.comments.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentApplicationService {
    private CommentRepository commentRepository;
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
