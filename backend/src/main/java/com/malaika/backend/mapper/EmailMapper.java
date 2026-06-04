package com.malaika.backend.mapper;

import com.malaika.backend.dto.EmailDto;
import com.malaika.backend.entity.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailMapper {

    @Mapping(target = "contact", ignore = true)
    Email toEntity(EmailDto dto);

    @Mapping(source = "contact.id", target = "contactId")
    EmailDto toDto(Email email);
}