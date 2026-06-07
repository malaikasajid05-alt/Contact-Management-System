package com.malaika.backend.mapper;

import com.malaika.backend.dto.ContactDetailDto;
import com.malaika.backend.entity.ContactDetail;
import com.malaika.backend.enums.DetailType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ContactDetailMapperTest {

    private final ContactDetailMapper mapper = new ContactDetailMapper() {
        @Override
        public ContactDetail toEntity(ContactDetailDto dto) {
            return new ContactDetail();
        }

        @Override
        public ContactDetailDto toDto(ContactDetail entity) {
            return new ContactDetailDto();
        }
    };


    @Test
    void mapStringToDetailType_phone_returnsPhoneEnum() {
        DetailType result = mapper.map("PHONE");
        assertEquals(DetailType.PHONE, result);
    }

    @Test
    void mapStringToDetailType_email_returnsEmailEnum() {
        DetailType result = mapper.map("EMAIL");
        assertEquals(DetailType.EMAIL, result);
    }

    @Test
    void mapStringToDetailType_null_returnsNull() {
        DetailType result = mapper.map((String) null);
        assertNull(result);
    }

    @Test
    void mapDetailTypeToString_phone_returnsPhoneString() {
        String result = mapper.map(DetailType.PHONE);
        assertEquals("PHONE", result);
    }

    @Test
    void mapDetailTypeToString_email_returnsEmailString() {
        String result = mapper.map(DetailType.EMAIL);
        assertEquals("EMAIL", result);
    }

    @Test
    void mapDetailTypeToString_null_returnsNull() {
        String result = mapper.map((DetailType) null);
        assertNull(result);
    }
}