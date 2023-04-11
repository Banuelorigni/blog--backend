package com.blog.adapter.articles;

import com.blog.adapter.articles.dto.CreateArticleRequest;
import com.blog.adapter.articles.mapper.ArticleDtoMapper;
import com.blog.adapter.articles.swaggers.GetArticles;
import com.blog.adapter.articles.swaggers.GetOneArticle;
import com.blog.application.articles.ArticleApplicationService;
import com.blog.application.articles.exceptions.ArticleNotFoundException;
import com.blog.application.tags.TagService;
import com.blog.application.tags.exceptions.TagNotFoundException;
import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@io.swagger.v3.oas.annotations.tags.Tag(name = "Article API")
public class ArticleController {
    private ArticleApplicationService articleApplicationService;
    private TagService tagService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Save article")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Saved Article",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Article.class))
                            }),
                    @ApiResponse(responseCode = "404", description = "Tag not found"),
            }
    )
    public Article createArticles(@Parameter(description = "info of saving article") @RequestBody @Valid CreateArticleRequest articleRequest) throws TagNotFoundException {
        Integer wordNumbers = articleApplicationService.countWordNumber(articleRequest.getContent());
        List<Tag> tags = tagService.findById(articleRequest.getTags());
        String html = articleApplicationService.markdownToHtml(articleRequest.getContent());

        Article article = ArticleDtoMapper.MAPPER.toModel(articleRequest, html, wordNumbers, tags);

        return articleApplicationService.createArticles(article);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @GetArticles
    @Operation(summary = "Get all articles")
    @PermitAll
    public Page<Article> getAllArticles(@RequestParam(required = false, defaultValue = "DESC") String orderBy,
                                        @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "5") int size) throws ArticleNotFoundException {
        return articleApplicationService.getAllArticles(orderBy, sortBy, page, size);
    }

    @GetMapping("/{articleId}")
    @PermitAll
    @GetOneArticle
    @Operation(summary = "Get one article")
    @ResponseStatus(HttpStatus.OK)
    public Article getArticleById(@PathVariable Long articleId) throws ArticleNotFoundException {
        return articleApplicationService.getArticleById(articleId);
    }
}
