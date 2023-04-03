package com.blog.adapter.articles.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateArticleRequest {
    @NotBlank(message = "请输入文章名称！")
    @Size(max = 100,message = "文章名称过长！")
    private String title;
    @NotBlank(message = "请输入文章内容！")
    @Size(max = 100000,message = "文章内容过长！")
    private String content;

    @NotBlank(message = "请添加封面！")
    private String coverUrl;

    private List<Long> tags;

}
