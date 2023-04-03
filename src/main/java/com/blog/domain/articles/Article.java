package com.blog.domain.articles;

import com.blog.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long id;

    private String title;

    private String content;

    private Integer wordNumbers;

    private String coverUrl;
    private Instant createdAt;
    private List<Tag> tags;

}
