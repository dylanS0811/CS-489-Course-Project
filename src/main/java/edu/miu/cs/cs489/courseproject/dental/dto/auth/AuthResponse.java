package edu.miu.cs.cs489.courseproject.dental.dto.auth;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresInMs,
        String username,
        String fullName,
        String role
) {
}
