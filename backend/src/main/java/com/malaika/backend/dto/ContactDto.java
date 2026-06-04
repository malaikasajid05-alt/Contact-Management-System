package com.malaika.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String title;

    private Long userId;

    private List<EmailDto> emails;

    private List<PhoneNoDto> phoneNos;
}