package com.blog.application.comments;

import com.blog.adapter.comments.repository.CommentRepository;
import com.blog.domain.comments.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Transactional
class CommentApplicationServiceTest {
    @InjectMocks
    private CommentApplicationService commentApplicationService;
    @Mock
    private CommentRepository commentRepository;
    private Comment comment;

    @BeforeEach
    void initializeComment() {
        comment = Comment.builder()
                .id(1L)
                .userName("test")
                .article_id(1L)
                .content("test test 1234")
                .build();
    }

    @Test
    void should_save_comment() {
        when(commentApplicationService.save(comment)).thenReturn(comment);
        Comment save = commentApplicationService.save(comment);

        assertEquals(comment.getId(), save.getId());
        assertEquals(comment.getContent(), save.getContent());
        assertEquals(comment.getArticle_id(), save.getArticle_id());
        assertEquals(comment.getUserName(), save.getUserName());

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void should_get_comment_by_articleId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.fromString("DESC"), "createdAt"));
        List<Comment> commentList = List.of(comment);
        when(commentApplicationService.getCommentByArticleId(1L,"DESC", "createdAt", 0, 10))
                .thenReturn(new PageImpl<>(commentList,pageable,commentList.size()));

        Page<Comment> commentByArticleId = commentApplicationService.getCommentByArticleId(1L, "DESC", "createdAt", 0, 10);

        assertNotNull(commentByArticleId);
        assertEquals(1,         commentByArticleId.getTotalElements());
        assertEquals(1,         commentByArticleId.getTotalPages());
        assertEquals(10,         commentByArticleId.getPageable().getPageSize());
        verify(commentRepository).getCommentByArticleId(1L, "DESC", "createdAt", 0, 10);
    }

}
