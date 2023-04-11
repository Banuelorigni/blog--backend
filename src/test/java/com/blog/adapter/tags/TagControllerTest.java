package com.blog.adapter.tags;

import com.blog.BlogApplication;
import com.blog.application.tags.TagApplicationService;
import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import com.blog.support.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BlogApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@Transactional
class TagControllerTest {
    @MockBean
    private TagApplicationService tagApplicationService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @Sql({"classpath:scripts/insert_an_admin_user.sql"})
    void should_create_tag_successfully() throws Exception {
        Tag tag = Tag.builder().id(1L).name("spring").build();
        when(tagApplicationService.createTag("spring")).thenReturn(tag);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString("spring"))
                        .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(1L, "ADMIN", "libingbing")))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        assertNotNull(response);
    }

    @Test
    @Sql({"classpath:scripts/insert_a_portal_user.sql"})
    void should_return_forbidden_when_create_tag_for_portal_user() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tags")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(""))
                        .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(2L, "PORTAL_USER", "portal_user")))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql({"classpath:scripts/insert_an_admin_user.sql"})
    void should_get_all_tags() throws Exception {
        List<Tag> tags = List.of(Tag.builder().id(1L).name("spring").build());
        when(tagApplicationService.findAll()).thenReturn(tags);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/tags/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(1L, "ADMIN", "libingbing")))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertNotNull(new ObjectMapper().writeValueAsString(tags), response.getContentAsString());
    }

    @Test
    @Sql({"classpath:scripts/insert_an_admin_user.sql"})
    void should_get_all_articles_by_tag_id() throws Exception {
        List<Article> articleList = Arrays.asList(
                Article.builder().id(1L).build(),
                Article.builder().id(2L).build()
        );
        when(tagApplicationService.findArticlesByTagId(1L))
                .thenReturn(articleList);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/tags/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(1L, "ADMIN", "libingbing")))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertNotNull(response);
    }
}
