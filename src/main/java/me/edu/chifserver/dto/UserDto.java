package me.edu.chifserver.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto (
        @NotEmpty
        String _id,
        @NotEmpty
        String name,
        @NotEmpty
        String username,
        @NotEmpty
        String bannerImageUrl,
        @NotEmpty
        String profileImageUrl
) {
}
