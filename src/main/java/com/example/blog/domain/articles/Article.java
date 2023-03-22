package com.example.blog.domain.articles;

import com.example.blog.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private List<Tag> tags;

}
