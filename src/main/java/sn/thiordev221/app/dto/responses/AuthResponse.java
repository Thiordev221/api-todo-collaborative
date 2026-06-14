package sn.thiordev221.app.dto.responses;

import java.util.List;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    Long userId,
    String email,
    String pseudo,
    List<String> roles    // String plutôt que Role — le frontend n'a pas ton enum
) {
    // Constructeur pratique — tokenType toujours "Bearer"
    public AuthResponse(String accessToken,
                        String refreshToken,
                        Long userId,
                        String email,
                        String pseudo,
                        List<String> roles) {
        this(accessToken, refreshToken, "Bearer", userId, email, pseudo, roles);
    }
}