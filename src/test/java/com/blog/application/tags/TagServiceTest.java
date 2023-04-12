package com.blog.application.tags;

import com.blog.BlogApplication;
import com.blog.application.tags.exceptions.TagNotFoundException;
import com.blog.domain.tag.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = BlogApplication.class)
@ActiveProfiles("test")
@Transactional
class TagServiceTest {

    @Autowired
    private TagService tagService;

    @Test
    @Sql({"classpath:scripts/insert_tags.sql"})
    void should_find_tags_by_tagId_list() {
        List<Long> tagIdList = List.of(1L, 3L);
        List<Tag> allTagById = tagService.findById(tagIdList);

        assertNotNull(allTagById);
    }

    @Test
    void should_throw_TagNotFoundException_when_tag_not_exist() {
        List<Long> tags = List.of(999L);

        assertThrows(TagNotFoundException.class, () -> tagService.findById(tags));

    }
}
