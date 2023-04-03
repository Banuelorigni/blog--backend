package com.blog.adapter.articles.swaggers;

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
public @interface SaveArticles {
    String summary() default "Save article";
    ApiResponse[] apiResponses() default {
            @ApiResponse(responseCode = "201", description = "Saved Article",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Article.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Tag not found",
                    content = @Content),
    };
}

