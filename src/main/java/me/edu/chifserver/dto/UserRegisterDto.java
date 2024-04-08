package me.edu.chifserver.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserRegisterDto(
        @NotEmpty
        String name,
        @NotEmpty
        String username,
        String base64Profile,
        String profileImgType,
        String base64Banner,
        String bannerImgType
) {
}
