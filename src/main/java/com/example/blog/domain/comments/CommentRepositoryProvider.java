package com.example.blog.domain.comments;

import com.example.blog.application.articles.exceptions.ArticleNotFoundException;
import com.example.blog.application.comments.exceptions.CommentNotFoundException;
import com.example.blog.domain.articles.JpaArticleRepository;
import com.example.blog.domain.comments.mapper.CommentEntityMapper;
import com.example.blog.infrastructure.articles.ArticlesEntity;
import com.example.blog.infrastructure.comments.CommentEntity;
import com.example.blog.infrastructure.user.JpaUserRepository;
import com.example.blog.infrastructure.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CommentRepositoryProvider implements CommentRepository {
    private final JpaCommentRepository jpaCommentRepository;
    private final JpaArticleRepository jpaArticleRepository;
    private final JpaUserRepository jpaUserRepository;

    @Override
    public Comment save(Comment comment) {
        ArticlesEntity article = jpaArticleRepository
                .findById(comment.getArticle_id())
                .orElseThrow(() -> new ArticleNotFoundException("article"));
        UserEntity user = jpaUserRepository.findByUsername(comment.getUserName()).orElseThrow(() -> new UsernameNotFoundException("user"));

        CommentEntity entity = CommentEntityMapper.MAPPER.toEntity(comment, article, user);
        CommentEntity commentEntity = jpaCommentRepository.save(entity);
        return CommentEntityMapper.MAPPER.toModel(commentEntity);
    }

    @Override
    public List<Comment> getCommentByArticleId(Long articleId) {
        List<CommentEntity> commentEntities = jpaCommentRepository.findAllByArticleId(articleId);
        if (commentEntities.size() == 0) {
            throw new CommentNotFoundException("article" + articleId + "的评论");
        }
        return commentEntities.stream().map(CommentEntityMapper.MAPPER::toModel).collect(Collectors.toList());
    }
}
