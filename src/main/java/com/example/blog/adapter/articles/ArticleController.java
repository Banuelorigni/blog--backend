package com.example.blog.adapter.articles;

import com.example.blog.adapter.articles.dto.CreateArticleRequest;
import com.example.blog.adapter.swaggers.GetArticles;
import com.example.blog.adapter.swaggers.SaveArticles;
import com.example.blog.application.articles.ArticleApplicationService;
import com.example.blog.application.articles.exceptions.ArticleNotFoundException;
import com.example.blog.application.tags.TagService;
import com.example.blog.application.tags.exceptions.TagNotFoundException;
import com.example.blog.domain.articles.Article;
import com.example.blog.domain.tag.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/articles")
@Validated
@AllArgsConstructor
@Transactional
public class ArticleController {
    private ArticleApplicationService articleApplicationService;
    private TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SaveArticles
    public Article createArticles(@Parameter(description = "info of saving article") @RequestBody @Valid CreateArticleRequest articleRequest) throws TagNotFoundException {
        Integer wordNumbers = articleApplicationService.countWordNumber(articleRequest.getContent());
        List<Tag> tags = tagService.findById(articleRequest.getTags());
        String html = articleApplicationService.markdownToHtml(articleRequest.getContent());

        Article article = ArticleDtoMapper.MAPPER.toModel(articleRequest, html, wordNumbers, tags);

        return articleApplicationService.createArticles(article);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @GetArticles
    public Page<Article> getAllArticles(@RequestParam(required = false, defaultValue = "DESC") String orderBy,
                                        @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "5") int size) throws ArticleNotFoundException {
        return articleApplicationService.getAllArticles(orderBy,sortBy,page,size);
    }

}
