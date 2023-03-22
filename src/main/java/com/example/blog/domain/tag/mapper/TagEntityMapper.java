package com.example.blog.domain.tag.mapper;

import com.example.blog.domain.tag.Tag;
import com.example.blog.infrastructure.tags.TagsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagEntityMapper {
    TagEntityMapper MAPPER = Mappers.getMapper(TagEntityMapper.class);
    TagsEntity toEntity(Tag tags);


    Tag toModel(TagsEntity tagInfo);
}
