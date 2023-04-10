package com.blog.adapter.articles;

import com.blog.BlogApplication;
import com.blog.adapter.articles.repository.ArticleRepositoryProvider;
import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = BlogApplication.class)
@ActiveProfiles("test")
@Transactional
class ArticleRepositoryProviderTest {
    @Autowired
    private ArticleRepositoryProvider articleRepositoryProvider;

    private Article article;
    private final Tag tag1 = new Tag();
    private final Tag tag2 = new Tag();
    private final List<Tag> tags = new ArrayList<>();

    @BeforeEach
    void initializeTags() {
        tag1.setId(1L);
        tag1.setName("spring");
        tag2.setId(3L);
        tag2.setName("C");
        tags.add(tag1);
        tags.add(tag2);
    }

    @BeforeEach
    void initializeArticle() {
        article = Article.builder()
                .title("文章1")
                .tags(tags)
                .content("这是一篇关于测试数据的文章内容。")
                .wordNumbers(16)
                .coverUrl("https://example.com/cover1.jpg")
                .createdAt(Instant.now())
                .build();

    }

    @Test
    @Sql({"classpath:scripts/insert_tags.sql"})
    void should_save_article() {
        Article save = articleRepositoryProvider.save(article);

        assertEquals(article.getContent(), save.getContent());
        assertEquals(article.getCoverUrl(), save.getCoverUrl());
        assertEquals(article.getTitle(), save.getTitle());
        assertEquals(article.getWordNumbers(), save.getWordNumbers());

        List<Tag> saveTags = save.getTags();

        assertEquals(tags.get(0).getName(), saveTags.get(0).getName());
        assertEquals(tags.get(1).getName(), saveTags.get(1).getName());
    }

    @Test
    @Sql({"classpath:scripts/insert_articles.sql"})
    void should_get_all_articles() {
        Page<Article> allArticles = articleRepositoryProvider.getAllArticles("DESC", "createdAt", 0, 5);

        assertEquals(3,allArticles.getTotalElements());
        assertEquals(1,allArticles.getTotalPages());
        assertEquals(5,allArticles.getPageable().getPageSize());
        assertNotNull(allArticles);
    }
    @Test
    @Sql({"classpath:scripts/insert_articles.sql",
            "classpath:scripts/insert_tags.sql",
            "classpath:scripts/insert_articles_tags_record.sql"})
    void should_get_article_by_id(){
        Article articleById = articleRepositoryProvider.getArticleById(1L);

        assertEquals(1L,articleById.getId());
        assertEquals("article1",articleById.getTitle());
        assertEquals(12,articleById.getWordNumbers());
        assertEquals("article test 1",articleById.getContent());
        assertEquals("https://example.com/cover1.jpg",articleById.getCoverUrl());
        Assertions.assertEquals("spring",articleById.getTags().get(0).getName());
        Assertions.assertEquals("C",articleById.getTags().get(1).getName());
    }

}
