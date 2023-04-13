package com.blog.adapter.tags;

import com.blog.adapter.tags.swaggers.GetArticlesByTagId;
import com.blog.adapter.tags.swaggers.GetTags;
import com.blog.application.tags.TagApplicationService;
import com.blog.domain.articles.Article;
import com.blog.domain.tag.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
@Validated
@AllArgsConstructor
@Transactional
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag API")
public class TagController {
    private TagApplicationService tagApplicationService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "save tag")
    public Tag createTag(@RequestBody @Valid String tag) {
        return tagApplicationService.createTag(tag);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetTags
    @Operation(summary = "Get all tags")
    public List<Tag> getAllTags() {
        return tagApplicationService.findAll();
    }

    @GetMapping("/{tagId}")
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetArticlesByTagId
    @Operation(summary = "Get articles by tagId")
    public List<Article> getArticlesByTagId(@PathVariable Long tagId) {
        return tagApplicationService.findArticlesByTagId(tagId);
    }
}
