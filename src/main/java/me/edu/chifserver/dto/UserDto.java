package me.edu.chifserver.dto;

public record UserDto (
        String _id,
        String name,
        String username,
        String bannerImageUrl,
        String profileImageUrl
) {
}
