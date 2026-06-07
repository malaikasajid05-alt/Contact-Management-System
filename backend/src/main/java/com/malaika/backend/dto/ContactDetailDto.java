package com.malaika.backend.dto;

import lombok.Data;

@Data
public class ContactDetailDto {

    private Long id;
    private String type;
    private String value;
    private String label;
    private Long contactId;
}