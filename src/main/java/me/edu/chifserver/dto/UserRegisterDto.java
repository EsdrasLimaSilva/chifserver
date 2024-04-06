package me.edu.chifserver.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserRegisterDto(
        @NotEmpty
        String name,
        @NotEmpty
        String username,
        @NotEmpty
        String base64Profile,
        @NotEmpty
        String base64Banner
) {
}
