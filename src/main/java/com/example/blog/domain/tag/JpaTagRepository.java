package com.example.blog.domain.tag;

import com.example.blog.infrastructure.tags.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTagRepository extends JpaRepository<TagsEntity,Long> {
}
