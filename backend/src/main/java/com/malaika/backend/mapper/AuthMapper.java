package com.malaika.backend.mapper;

import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.dto.RegisterResponseDto;
import com.malaika.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target ="id", ignore = true)
    @Mapping(target ="password", ignore = true)
    User toEntity(RegisterRequestDto dto);
    RegisterResponseDto toDto(User user);
    

}
