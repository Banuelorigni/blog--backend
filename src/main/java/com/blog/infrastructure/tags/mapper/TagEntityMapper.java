package com.blog.infrastructure.tags.mapper;

import com.blog.domain.tag.Tag;
import com.blog.infrastructure.tags.TagsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagEntityMapper {
    TagEntityMapper MAPPER = Mappers.getMapper(TagEntityMapper.class);
    TagsEntity toEntity(Tag tags);


    Tag toModel(TagsEntity tagInfo);

    List<Tag> toEntity(List<TagsEntity> tags);
    @Mapping(source = "tag", target = "name")
    TagsEntity toEntity(String tag);
}
