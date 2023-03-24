package com.example.blog.adapter.tags;

import com.example.blog.application.tags.TagApplicationService;
import com.example.blog.application.tags.TagService;
import com.example.blog.application.tags.exceptions.TagNotFoundException;
import com.example.blog.domain.articles.Article;
import com.example.blog.domain.tag.Tag;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Tag> getAllTags() throws TagNotFoundException {
        return tagApplicationService.findAll();
    }

    @GetMapping("/{tagName}")
    public List<Article> getArticlesByTag(@PathVariable String tagName) {
        return null;
    }
}
