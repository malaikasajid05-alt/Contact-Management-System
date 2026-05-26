package com.malaika.backend.mapper;

import com.malaika.backend.dto.RegisterRequestDto;
import com.malaika.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    User toEntity(RegisterRequestDto dto);

    

}
