package com.example.blog.adapter.tags;

import com.example.blog.adapter.tags.swaggers.GetArticlesByTagId;
import com.example.blog.adapter.tags.swaggers.GetTags;
import com.example.blog.adapter.tags.swaggers.SaveTag;
import com.example.blog.application.tags.TagApplicationService;
import com.example.blog.application.tags.exceptions.TagNotFoundException;
import com.example.blog.domain.articles.Article;
import com.example.blog.domain.tag.Tag;
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
public class TagController {
    private TagApplicationService tagApplicationService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PermitAll
    @SaveTag
    public Tag createTag(@RequestBody @Valid String tag) {
        return tagApplicationService.createTag(tag);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PermitAll
    @GetTags
    public List<Tag> getAllTags() throws TagNotFoundException {
        return tagApplicationService.findAll();
    }

    @GetMapping("/{tagId}")
    @PermitAll
    @GetArticlesByTagId
    public List<Article> getArticlesByTagId(@PathVariable Long tagId) {
        return tagApplicationService.findArticlesByTagId(tagId);
    }
}
