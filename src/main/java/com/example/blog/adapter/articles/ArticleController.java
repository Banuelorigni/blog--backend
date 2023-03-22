package com.example.blog.adapter.articles;

import com.example.blog.adapter.articles.dto.CreateArticleRequest;
import com.example.blog.adapter.swaggers.SaveArticles;
import com.example.blog.application.articles.ArticleApplicationService;
import com.example.blog.application.tags.TagService;
import com.example.blog.application.tags.exceptions.TagNotFoundException;
import com.example.blog.domain.articles.Article;
import com.example.blog.domain.tag.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

        Article article = ArticleDtoMapper.MAPPER.toModel(articleRequest, wordNumbers, tags);

        return articleApplicationService.createArticles(article);
    }
}
