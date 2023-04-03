package com.blog.domain.comments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long id;
    private String content;

    private String userName;
    private Long article_id;
}
