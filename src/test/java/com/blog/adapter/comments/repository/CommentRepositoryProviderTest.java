package com.blog.adapter.comments.repository;

import com.blog.BlogApplication;
import com.blog.domain.comments.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BlogApplication.class)
@ActiveProfiles("test")
@Transactional
@Sql({"classpath:scripts/insert_a_portal_user.sql",
        "classpath:scripts/insert_articles.sql"})
class CommentRepositoryProviderTest {

    @Autowired
    private CommentRepositoryProvider commentRepositoryProvider;
//
//    @Test
//    @Sql({"classpath:scripts/insert_comments.sql"})
//    void should_get_comment_by_article_id() {
//        Page<Comment> commentByArticleId = commentRepositoryProvider.getCommentByArticleId(1L, "DESC", "createdAt", 0, 10);
//
//        assertEquals(1, commentByArticleId.getTotalElements());
//        assertEquals(1, commentByArticleId.getTotalPages());
//        assertEquals(10, commentByArticleId.getPageable().getPageSize());
//        assertNotNull(commentByArticleId);
//    }
//
//
//    @Test
//    void should_save_comment() {
//        Comment comment = Comment.builder().content("test").article_id(1L).userName("portal_user").build();
//
//        Comment save = commentRepositoryProvider.save(comment);
//
//        assertEquals(comment.getUserName(), save.getUserName());
//        assertEquals(comment.getArticle_id(), save.getArticle_id());
//        assertEquals(comment.getContent(), save.getContent());
//    }
}
