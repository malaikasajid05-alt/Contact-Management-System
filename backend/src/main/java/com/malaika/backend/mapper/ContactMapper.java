package com.malaika.backend.mapper;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {EmailMapper.class, PhoneNoMapper.class}
)
public interface ContactMapper {

        @Mapping(target = "user", ignore = true)
        Contact toEntity(ContactDto dto);

        @Mapping(source = "user.id", target = "userId")
        ContactDto toDto(Contact contact);
    }

