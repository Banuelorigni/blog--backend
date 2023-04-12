package com.blog.adapter.comments.repository;

import com.blog.BlogApplication;
import com.blog.application.articles.exceptions.ArticleNotFoundException;
import com.blog.domain.comments.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = BlogApplication.class)
@ActiveProfiles("test")
@Transactional
class CommentRepositoryProviderTest {

    @Autowired
    private CommentRepositoryProvider commentRepositoryProvider;


    @Test
    @Sql({"classpath:scripts/insert_a_portal_user.sql",
            "classpath:scripts/insert_articles.sql"})
    void should_save_comment() {
        Comment comment = Comment.builder().content("test").article_id(1L).userName("portal_user").build();

        Comment save = commentRepositoryProvider.save(comment);

        assertEquals(comment.getUserName(), save.getUserName());
        assertEquals(comment.getArticle_id(), save.getArticle_id());
        assertEquals(comment.getContent(), save.getContent());
    }

    @Test
    @Sql({"classpath:scripts/insert_a_portal_user.sql"})
    void should_throw_ArticleNotFoundException_when_user_not_exist() {
        Comment comment = Comment.builder().content("test").article_id(1L).userName("portal_user").build();

        assertThrows(ArticleNotFoundException.class, ()-> commentRepositoryProvider.save(comment));
    }
    @Test
    @Sql({"classpath:scripts/insert_articles.sql"})
    void should_throw_UsernameNotFoundException_when_user_not_exist() {
        Comment comment = Comment.builder().content("test").article_id(1L).userName("portal_user").build();

        assertThrows(UsernameNotFoundException.class, ()-> commentRepositoryProvider.save(comment));
    }

    @Test
    @Sql({"classpath:scripts/insert_articles.sql",
            "classpath:scripts/insert_a_portal_user.sql",
            "classpath:scripts/insert_comments.sql"})
    void should_get_comment_by_article_id() {
        Page<Comment> commentByArticleId = commentRepositoryProvider.getCommentByArticleId(1L, "DESC", "createdAt", 0, 10);

        assertEquals(2, commentByArticleId.getTotalElements());
        assertEquals(1, commentByArticleId.getTotalPages());
        assertEquals(10, commentByArticleId.getPageable().getPageSize());
        assertNotNull(commentByArticleId);
    }

    @Test
    @Sql({"classpath:scripts/insert_a_portal_user.sql",
            "classpath:scripts/insert_articles.sql"})
    void should_return_null_when_comment_not_exist() {
        Page<Comment> commentByArticleId = commentRepositoryProvider.getCommentByArticleId(1L, "DESC", "createdAt", 0, 10);

        assertNull(commentByArticleId);
    }

    @Test
    @Sql({"classpath:scripts/insert_articles.sql",
            "classpath:scripts/insert_a_portal_user.sql",
            "classpath:scripts/insert_comments.sql"})
    void should_sort_by_ASC() {
        Page<Comment> commentByArticleId = commentRepositoryProvider.getCommentByArticleId(1L, "ASC", "id", 0, 10);

        assertEquals(1, commentByArticleId.getContent().get(0).getId());
    }
}
