package com.blog.adapter.articles;

import com.blog.BlogApplication;
import com.blog.adapter.articles.dto.CreateArticleRequest;
import com.blog.adapter.articles.mapper.ArticleDtoMapper;
import com.blog.application.articles.ArticleApplicationService;
import com.blog.application.articles.exceptions.ArticleNotFoundException;
import com.blog.application.tags.TagService;
import com.blog.application.tags.exceptions.TagNotFoundException;
import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import com.blog.support.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = BlogApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureJsonTesters
@Transactional
class ArticleControllerTest {
    @MockBean
    private ArticleApplicationService articleApplicationService;
    @MockBean
    private TagService tagService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private JacksonTester<Article> articleJson;
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

    @Nested
    class SaveArticles {

        private final CreateArticleRequest
                createArticleRequest = new CreateArticleRequest();
        @BeforeEach
        void initializeArticleRequest(){
            createArticleRequest.setTitle("Richardson Maturity Model");
            createArticleRequest.setCoverUrl("https://images.unsplash.com/photo-1672243776760-67aec979f591?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80");
            createArticleRequest.setContent(" Roy Fielding proposed the REST architecture in his doctoral thesis, and he also proposed a concept called Richardson maturity model for evaluating the maturity of RESTful apis. The model includes the following four levels: \n \n ### Level 1: use the HTTP method \n \n At level 1, the API starts using HTTP methods (GET, POST, PUT, DELETE) to define resource operations. ");
        }

        @Test
        @Sql({"classpath:scripts/insert_an_admin_user.sql",
                "classpath:scripts/insert_tags.sql"})
        void should_save_article_successfully() throws Exception {
            createArticleRequest.setTags(List.of(1L, 3L));

            String html = "Roy Fielding proposed the REST architecture in his doctoral thesis, and he also proposed a concept called Richardson maturity model for evaluating the maturity of RESTful apis. The model includes the following four levels: \\n \\n ### Level 1: use the HTTP method \\n \\n At level 1, the API starts using HTTP methods (GET, POST, PUT, DELETE) to define resource operations. \"";
            Article article = ArticleDtoMapper.MAPPER.toModel(createArticleRequest, html, 392, tags);

            when(articleApplicationService.countWordNumber(createArticleRequest.getContent())).thenReturn(392);
            when(tagService.findById(createArticleRequest.getTags())).thenReturn(tags);
            when(articleApplicationService.markdownToHtml(createArticleRequest.getContent())).thenReturn(html);
            when(articleApplicationService.createArticles(any(Article.class))).thenReturn(article);

            MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                            .post("/articles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createArticleRequest))
                            .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(1L, "ADMIN", "libingbing")))
                    )
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse();

            assertEquals(articleJson.write(article).getJson(),response.getContentAsString());
            verify(articleApplicationService).createArticles(any(Article.class));
            verify(tagService).findById(createArticleRequest.getTags());
            verify(articleApplicationService).countWordNumber(createArticleRequest.getContent());
            verify(articleApplicationService).markdownToHtml(createArticleRequest.getContent());
        }

        @Test
        @Sql({"classpath:scripts/insert_a_portal_user.sql"})
        void should_return_forbidden_when_save_course_for_portal_user() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/articles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createArticleRequest))
                            .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(2L, "PORTAL_USER", "portal_user")))
                    )
                    .andExpect(status().isForbidden());

        }

        @Test
        @Sql({"classpath:scripts/insert_an_admin_user.sql",
                "classpath:scripts/insert_tags.sql"})
        void should_save_throw_TagNotFoundException_when_tag_id_not_exist() throws Exception {
            List<Long> tagIdList = List.of(999L);
            createArticleRequest.setTags(tagIdList);

            doThrow(new TagNotFoundException("Tag")).when(tagService).findById(tagIdList);

            mockMvc.perform(MockMvcRequestBuilders
                            .post("/articles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().writeValueAsString(createArticleRequest))
                            .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(1L, "ADMIN", "libingbing")))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Tag不存在"));

        }

    }
@Nested
class GetAllArticles{
        private List<Article> articles;
    @BeforeEach
    void initializeArticleList(){
         articles = new ArrayList<>(Arrays.asList(
                 Article.builder().build(),
                 Article.builder().build(),
                 Article.builder().build(),
                 Article.builder().build(),
                 Article.builder().build()
         ));
    }

    @Test
    void should_get_all_articles() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.fromString("DESC"), "createdAt"));

        when(articleApplicationService.getAllArticles("DESC", "createdAt", 0, 5))
                .thenReturn(new PageImpl<>(articles, pageable, articles.size()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/articles/all")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.pageable.pageSize").value(5))
                .andExpect(jsonPath("$.totalPages").value(1));
    }
    @Test
    void should_return_two_pages_when_get_all_articles() throws Exception {

        articles.add(Article.builder().build());
        articles.add(Article.builder().build());
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.fromString("DESC"), "createdAt"));

        when(articleApplicationService.getAllArticles("DESC", "createdAt", 0, 5))
                .thenReturn(new PageImpl<>(articles, pageable, articles.size()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/articles/all")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(7))
                .andExpect(jsonPath("$.pageable.pageSize").value(5))
                .andExpect(jsonPath("$.totalPages").value(2));

    }

}
@Nested
class GetArticleById{

    @Test
    @Sql({"classpath:scripts/insert_articles.sql",
            "classpath:scripts/insert_tags.sql"})
    void should_get_article_by_id() throws Exception {
        Article article = Article.builder().id(1L).title("article1").content("article test 1").wordNumbers(12).coverUrl("https://example.com/cover1.jpg").tags(tags).build();

        when(articleApplicationService.getArticleById(1L)).thenReturn(article);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .get("/articles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        verify(articleApplicationService).getArticleById(1L);
        assertEquals(new ObjectMapper().writeValueAsString(article), response.getContentAsString());
    }
    @Test
    void should_throw_ArticleNotFoundException_when_article_id_not_exist() throws Exception {
        doThrow(new ArticleNotFoundException("文章999")).when(articleApplicationService).getArticleById(999L);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/articles/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("文章999不存在"));

    }

}

}
