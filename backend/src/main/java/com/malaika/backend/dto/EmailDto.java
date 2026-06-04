package com.malaika.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {
    private Long id;
    private String email;
    private String eLabel;
    private Long contactId;
}
