package com.malaika.backend.mapper;

import com.malaika.backend.dto.ProfileResponseDto;
import com.malaika.backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
     ProfileResponseDto toDto(User user);
}
