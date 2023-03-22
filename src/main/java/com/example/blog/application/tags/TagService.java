package com.example.blog.application.tags;

import com.example.blog.application.tags.exceptions.TagNotFoundException;
import com.example.blog.domain.tag.Tag;
import com.example.blog.domain.tag.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {
    private TagRepository tagRepository;

    public List<Tag> findById(List<Long> tags) {
        List<Tag> allTagById = tagRepository.findAllTagById(tags);
        if (allTagById.size() == 0){
            throw new TagNotFoundException("Tag");
        }
        return allTagById;

    }
}
