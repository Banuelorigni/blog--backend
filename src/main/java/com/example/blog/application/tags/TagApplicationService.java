package com.example.blog.application.tags;

import com.example.blog.domain.tag.Tag;
import com.example.blog.domain.tag.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagApplicationService {
    private final TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
