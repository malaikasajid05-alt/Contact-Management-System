package com.malaika.backend.mapper;

import com.malaika.backend.dto.ContactDetailDto;
import com.malaika.backend.entity.ContactDetail;
import com.malaika.backend.enums.DetailType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContactDetailMapper {

    @Mapping(target = "contact", ignore = true)
    ContactDetail toEntity(ContactDetailDto dto);

    @Mapping(source = "contact.id", target = "contactId")
    @Mapping(source = "type", target = "type")
    ContactDetailDto toDto(ContactDetail entity);

    default DetailType map(String value) {
        return value == null ? null : DetailType.valueOf(value);
    }

    default String map(DetailType type) {
        return type == null ? null : type.name();
    }
}