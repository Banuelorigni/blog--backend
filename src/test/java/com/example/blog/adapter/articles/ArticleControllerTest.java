package com.example.blog.adapter.articles;

import com.example.blog.adapter.articles.dto.CreateArticleRequest;
import com.example.blog.application.articles.ArticleApplicationService;
import com.example.blog.application.tags.TagService;
import com.example.blog.domain.articles.Article;
import com.example.blog.domain.tag.Tag;
import com.example.blog.support.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
    private final CreateArticleRequest createArticleRequest = new CreateArticleRequest();
    private final Tag tag1 = new Tag();
    private final Tag tag2 = new Tag();
    private final List<Tag> tags = new ArrayList<>();
    @BeforeEach
    void initializeArticleRequest(){
        createArticleRequest.setTitle("Richardson Maturity Model");
        createArticleRequest.setTags(List.of(1L,3L));
        createArticleRequest.setCoverUrl("https://images.unsplash.com/photo-1672243776760-67aec979f591?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80");
        createArticleRequest.setContent(" Roy Fielding proposed the REST architecture in his doctoral thesis, and he also proposed a concept called Richardson maturity model for evaluating the maturity of RESTful apis. The model includes the following four levels: \n \n ### Level 1: use the HTTP method \n \n At level 1, the API starts using HTTP methods (GET, POST, PUT, DELETE) to define resource operations. ");
        tag1.setId(1L);
        tag1.setName("spring");
        tag2.setId(3L);
        tag2.setName("C");
        tags.add(tag1);
        tags.add(tag2);
    }
    @Test
    @Sql({"classpath:scripts/insert_an_admin_user.sql"})
    @Sql({"classpath:scripts/insert_tags.sql"})
    void should_save_article_successfully() throws Exception {
        String html = "Roy Fielding proposed the REST architecture in his doctoral thesis, and he also proposed a concept called Richardson maturity model for evaluating the maturity of RESTful apis. The model includes the following four levels: \\n \\n ### Level 1: use the HTTP method \\n \\n At level 1, the API starts using HTTP methods (GET, POST, PUT, DELETE) to define resource operations. \"";
        Article article = ArticleDtoMapper.MAPPER.toModel(createArticleRequest,html,392,tags);

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

        assertEquals(response.getContentAsString(), articleJson.write(article).getJson());
        verify(articleApplicationService).createArticles(any(Article.class));
        verify(tagService).findById(createArticleRequest.getTags());
        verify(articleApplicationService).countWordNumber(createArticleRequest.getContent());
        verify(articleApplicationService).markdownToHtml(createArticleRequest.getContent());
    }



}
