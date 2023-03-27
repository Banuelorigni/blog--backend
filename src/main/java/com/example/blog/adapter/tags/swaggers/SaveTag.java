package com.example.blog.adapter.tags.swaggers;

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
public @interface SaveTag {
    String summary() default "Save Tag";
    ApiResponse[] apiResponses() default {
            @ApiResponse(responseCode = "201", description = "Saved Tag",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Article.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Tag not found",
                    content = @Content),
    };
}

