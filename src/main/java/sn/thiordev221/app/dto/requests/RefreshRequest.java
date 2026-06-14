package sn.thiordev221.app.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
    @NotBlank(message = "Le refresh token est obligatoire")
    String refreshToken
) {}
