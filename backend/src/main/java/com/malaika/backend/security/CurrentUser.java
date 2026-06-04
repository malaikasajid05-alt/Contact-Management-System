package com.malaika.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {

    public Long getCurrentUserId() {
        return Long.valueOf(
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName()
        );
    }
}
