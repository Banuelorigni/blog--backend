package com.blog.adapter.tags.repository;

import com.blog.infrastructure.tags.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTagRepository extends JpaRepository<TagsEntity,Long> {
}
