package com.example.blog.adapter.articles;

import com.example.blog.adapter.articles.dto.CreateArticleRequest;
import com.example.blog.application.articles.ArticleApplicationService;
import com.example.blog.application.tags.TagService;
import com.example.blog.domain.tag.Tag;
import com.example.blog.support.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.List;

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
    private final CreateArticleRequest createArticleRequest = new CreateArticleRequest();
    private final Tag tag1 = new Tag();
    private final Tag tag2 = new Tag();
    private final List<Tag> tags = new ArrayList<>();
    @BeforeEach
    void initializeArticleRequest(){
        createArticleRequest.setTitle("Richardson 成熟度模型");
        createArticleRequest.setTags(List.of(1L,3L));
        createArticleRequest.setCoverUrl("https://images.unsplash.com/photo-1672243776760-67aec979f591?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2070&q=80");
        createArticleRequest.setContent(" Roy Fielding 在他的博士论文中提出了 REST 架构，他还提出了一个名为 Richardson 成熟度模型的概念，用于评估 RESTful API 的成熟程度。该模型包括以下四个级别：\n \n ### 级别 0：定义 URI \n \n 在级别 0 中，所有 API 路径都是唯一的，但它们不必遵循任何特定的约定。 \n \n ### 级别 1：使用 HTTP 方法 \n \n 在级别 1 中，API 开始使用 HTTP 方法（GET、POST、PUT、DELETE）来定义资源操作。\n \n ### 级别 2：使用 HTTP 状态码 \n \n 在级别 2 中，API 开始使用 HTTP 状态码来表示操作的结果。\n \n ### 级别 3：使用超媒体控制 \n \n 在级别 3 中，API 开始使用超媒体控制来定义资源之间的关系和操作。这意味着客户端可以通过 API 自动发现可用的操作。");
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
        when(articleApplicationService.countWordNumber(createArticleRequest.getContent())).thenReturn(392);
        when(tagService.findById(createArticleRequest.getTags())).thenReturn(tags);
        when(articleApplicationService.markdownToHtml(createArticleRequest.getContent())).thenReturn("<p>Roy Fielding 在他的博士论文中提出了 REST 架构，他还提出了一个名为 Richardson 成熟度模型的概念，用于评估 RESTful API 的成熟程度。该模型包括以下四个级别：</p>\\n<h3>级别 0：定义 URI</h3>\\n<p>在级别 0 中，所有 API 路径都是唯一的，但它们不必遵循任何特定的约定。</p>\\n<h3>级别 1：使用 HTTP 方法</h3>\\n<p>在级别 1 中，API 开始使用 HTTP 方法（GET、POST、PUT、DELETE）来定义资源操作。</p>\\n<h3>级别 2：使用 HTTP 状态码</h3>\\n<p>在级别 2 中，API 开始使用 HTTP 状态码来表示操作的结果。</p>\\n<h3>级别 3：使用超媒体控制</h3>\\n<p>在级别 3 中，API 开始使用超媒体控制来定义资源之间的关系和操作。这意味着客户端可以通过 API 自动发现可用的操作。</p>\\n");

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                        .post("/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createArticleRequest))
                        .cookie(new Cookie("blog_token", jwtUtils.createJwtToken(1L, "ADMIN", "libingbing")))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
    }

}
