package com.blog.application.articles;

import com.blog.domain.articles.Article;
import com.blog.adapter.articles.repository.ArticleRepository;
import com.blog.domain.tag.Tag;
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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ArticleApplicationServiceTest {
    @InjectMocks
    private ArticleApplicationService articleApplicationService;
    @Mock
    private ArticleRepository articleRepository;
    private Article article1;
    private final Tag tag1 = new Tag();
    private final Tag tag2 = new Tag();
    private final List<Tag> tags = new ArrayList<>();
    private List<Article> articles;

    @BeforeEach
    void initializeArticleList() {
        articles = new ArrayList<>(Arrays.asList(
                Article.builder().build(),
                Article.builder().build(),
                Article.builder().build(),
                Article.builder().build(),
                Article.builder().build()
        ));
    }

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
        article1 = Article.builder()
                .title("文章1")
                .tags(tags)
                .content("这是一篇关于测试数据的文章内容。")
                .wordNumbers(16)
                .coverUrl("https://example.com/cover1.jpg")
                .createdAt(Instant.now())
                .build();

    }

    @Test
    void should_save_article() {
        when(articleApplicationService.createArticles(article1)).thenReturn(article1);
        Article articleReturn = articleApplicationService.createArticles(article1);

        assertEquals(article1.getContent(), articleReturn.getContent());
        assertEquals(article1.getCoverUrl(), articleReturn.getCoverUrl());
        assertEquals(article1.getTitle(), articleReturn.getTitle());
        assertEquals(article1.getWordNumbers(), articleReturn.getWordNumbers());
        assertEquals(article1.getTags(), articleReturn.getTags());

        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void should_get_all_articles() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.fromString("DESC"), "createdAt"));

        when(articleApplicationService.getAllArticles("DESC", "createdAt", 0, 5))
                .thenReturn(new PageImpl<>(articles, pageable, articles.size()));
        Page<Article> articlesReturn = articleApplicationService.getAllArticles("DESC", "createdAt", 0, 5);

        assertNotNull(articlesReturn);
        assertEquals(5,    articlesReturn.getTotalElements());
        assertEquals(1,       articlesReturn.getTotalPages());
        assertEquals(5, articlesReturn.getPageable().getPageSize());
    }

    @Test
    void should_get_article_by_id(){
        when(articleApplicationService.getArticleById(1L)).thenReturn(article1);
        Article articleReturn = articleApplicationService.getArticleById(1L);

        assertEquals(article1.getContent(), articleReturn.getContent());
        assertEquals(article1.getCoverUrl(), articleReturn.getCoverUrl());
        assertEquals(article1.getTitle(), articleReturn.getTitle());
        assertEquals(article1.getWordNumbers(), articleReturn.getWordNumbers());
        assertEquals(article1.getTags(), articleReturn.getTags());

        verify(articleRepository).getArticleById(1L);
    }


}
