package com.example.blog.domain.tag;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TagRepository {

    void save(List<String> tags);

    List<Tag> findAllTagById(List<Long> tags);

    List<Tag> findAll();
}
