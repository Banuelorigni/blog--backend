package com.blog.domain.comments;

import com.blog.application.comments.exceptions.CommentNotFoundException;
import com.blog.domain.articles.JpaArticleRepository;
import com.blog.domain.comments.mapper.CommentEntityMapper;
import com.blog.infrastructure.articles.ArticlesEntity;
import com.blog.infrastructure.user.JpaUserRepository;
import com.blog.infrastructure.user.entity.UserEntity;
import com.blog.application.articles.exceptions.ArticleNotFoundException;
import com.blog.infrastructure.comments.CommentEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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
    public Page<Comment> getCommentByArticleId(Long articleId, String orderBy, String sortBy, int page, int size) {
        Sort.Direction sortDirection = orderBy.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page, size, sortDirection, sortBy);
        Page<CommentEntity> commentEntities = jpaCommentRepository.findAllByArticleId(articleId,pageRequest);

        //todo
        if (commentEntities.getTotalElements() == 0) {
            throw new CommentNotFoundException("article" + articleId + "的评论");
        }

        return commentEntities.map(CommentEntityMapper.MAPPER::toModel);
    }
}
