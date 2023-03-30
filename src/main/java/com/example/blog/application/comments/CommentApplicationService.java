package com.example.blog.application.comments;

import com.example.blog.domain.comments.Comment;
import com.example.blog.domain.comments.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommentApplicationService {
    private CommentRepository commentRepository;
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentByArticleId(Long articleId) {
        return commentRepository.getCommentByArticleId(articleId);
    }
}
