package com.example.blog.adapter.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserPageResponse {
    private Long totalElements;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalPage;
    private List<UserInfoResponse> contents;

    public UserPageResponse(Page<UserInfoResponse> pageData) {
        this.totalElements = pageData.getTotalElements();
        this.pageSize = pageData.getSize();
        this.pageNumber = pageData.getNumber() + 1;
        this.totalPage = pageData.getTotalPages();
        this.contents = pageData.getContent();
    }
}
