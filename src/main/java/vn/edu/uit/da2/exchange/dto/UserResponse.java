
package vn.edu.uit.da2.exchange.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserResponse {

    private final UUID userId;
    private final String username;
    private final String email;
    private final String fullName;
    private final LocalDateTime createdAt;

    public UserResponse(
            UUID userId,
            String username,
            String email,
            String fullName,
            LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
