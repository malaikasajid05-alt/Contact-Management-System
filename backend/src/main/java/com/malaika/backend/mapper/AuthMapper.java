package com.malaika.backend.mapper;

import com.malaika.backend.dto.AuthResponseDto;
import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target ="id", ignore = true)
    @Mapping(target ="password", ignore = true)
    @Mapping(target ="contacts", ignore = true)
    User toEntity(RegisterRequestDto dto);
    AuthResponseDto toDto(User user);
    

}
