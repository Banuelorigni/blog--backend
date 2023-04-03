package com.blog.adapter.comments.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    @NotBlank(message = "请输入评论内容！")
    @Size(max = 100,message = "评论内容过长！")
    private String content;
    @NotNull
    private Long article_id;
}
