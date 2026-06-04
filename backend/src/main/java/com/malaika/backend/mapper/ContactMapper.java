package com.malaika.backend.mapper;

import com.malaika.backend.dto.ContactDto;
import com.malaika.backend.entity.Contact;
import com.malaika.backend.mapper.ContactDetailMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = ContactDetailMapper.class
)
public interface ContactMapper {

    @Mapping(target = "user", ignore = true)
    Contact toEntity(ContactDto dto);

    @Mapping(source = "user.id", target = "userId")
    ContactDto toDto(Contact contact);
}