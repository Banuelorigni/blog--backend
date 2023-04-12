package com.blog.application.tags;

import com.blog.adapter.tags.repository.TagRepository;
import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TagApplicationServiceTest {
    @InjectMocks
    private TagApplicationService tagApplicationService;
    @Mock
    private TagRepository tagRepository;

    @Test
    @Sql({"classpath:scripts/insert_tags.sql"})
    void should_find_all_tags() {
        List<Tag> tagList = tagApplicationService.findAll();

        assertNotNull(tagList);
        verify(tagRepository).findAll();
    }

    @Test
    @Sql({"classpath:scripts/insert_articles.sql",
            "classpath:scripts/insert_tags.sql",
            "classpath:scripts/insert_articles_tags_record.sql"
    })
    void should_find_all_articles_by_tag_id() {
        List<Article> articles = tagApplicationService.findArticlesByTagId(1L);

        assertNotNull(articles);
        verify(tagRepository).findById(1L);
    }

    @Test
    void should_create_tag_successfully() {
        Tag tag = Tag.builder().id(1L).name("english").build();

        when(tagApplicationService.createTag(any())).thenReturn(tag);
        Tag savedTag = tagApplicationService.createTag("english");

        assertEquals(tag,savedTag);
        verify(tagRepository).saveTag(any());
    }
}

