package com.group.a.social_media_app.util;

import com.group.a.social_media_app.dto.UserRegistrationDTO;

public class ValidationUtils {

    public static boolean passwordsMatch(UserRegistrationDTO dto) {
        return dto.getPassword() != null &&
                dto.getPassword().equals(dto.getConfirmPassword());
    }
}