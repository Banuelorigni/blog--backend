package com.blog.adapter.tags.swaggers;

import com.blog.domain.articles.Article;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetArticlesByTagId {
    String summary() default "Get Articles By TagId";
    ApiResponse[] apiResponses() default {
            @ApiResponse(responseCode = "200", description = "Get Articles By TagId",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Article.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Article not found",
                    content = @Content),
    };
}

