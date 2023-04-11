package com.blog.adapter.tags.repository;

import com.blog.BlogApplication;
import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = BlogApplication.class)
@ActiveProfiles("test")
@Transactional
class TagRepositoryProviderTest {
    @Autowired
    private TagRepositoryProvider tagRepositoryProvider;

//    @Test
//    @Sql({"classpath:scripts/insert_articles.sql",
//            "classpath:scripts/insert_tags.sql",
//            "classpath:scripts/insert_articles_tags_record.sql"
//    })
//    void should_find_articles_by_tag_id() {
//        List<Article> articleList = tagRepositoryProvider.findById(1L);
//
//        assertEquals(1L, articleList.get(0).getId());
//        assertEquals("article1", articleList.get(0).getTitle());
//        assertEquals(12, articleList.get(0).getWordNumbers());
//    }

    @Test
    void should_save_tag() {
        Tag saveTag = tagRepositoryProvider.saveTag("spring");

        assertEquals("spring", saveTag.getName());
    }

    @Test
    @Sql({"classpath:scripts/insert_tags.sql"})
    void should_find_all_tags_by_tag_id() {
        List<Long> tags = List.of(1L, 3L);
        List<Tag> tagList = tagRepositoryProvider.findAllTagById(tags);

        assertEquals(1L, tagList.get(0).getId());
        assertEquals(3L, tagList.get(1).getId());
        assertEquals("spring", tagList.get(0).getName());
        assertEquals("C", tagList.get(1).getName());
    }
}
