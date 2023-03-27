package com.example.blog.adapter.articles.swaggers;

import com.example.blog.domain.articles.Article;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetOneArticle {
    String summary() default "Get one article";
    ApiResponse[] apiResponses() default {
            @ApiResponse(responseCode = "200", description = "Get one Article",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Article.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content),
    };
}

