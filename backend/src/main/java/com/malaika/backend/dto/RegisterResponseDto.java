package com.malaika.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponseDto {
    String name;
    String email;
    String pnumber;
    String token;

}
