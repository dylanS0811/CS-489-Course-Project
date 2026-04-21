package edu.miu.cs.cs489.courseproject.dental.dto.auth;

public record CurrentUserResponse(
        Long userId,
        String username,
        String fullName,
        String email,
        String role
) {
}
