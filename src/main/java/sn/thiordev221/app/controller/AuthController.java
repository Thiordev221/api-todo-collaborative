package sn.thiordev221.app.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.config.JwtProperties;
import sn.thiordev221.app.custom_exceptons.InvalidTokenException;
import sn.thiordev221.app.dto.requests.LoginRequest;
import sn.thiordev221.app.dto.requests.RegisterRequest;
import sn.thiordev221.app.dto.responses.AuthResponse;
import sn.thiordev221.app.service.implementations.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String COOKIE_NAME = "refreshToken";

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest user, HttpServletRequest httpRequest) {
        AuthResponse response = authService.register(user, httpRequest);
        return withRefreshCookie(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest user, HttpServletRequest request) {
        AuthResponse response = authService.login(user, request);
        return withRefreshCookie(response, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(name = COOKIE_NAME, required = false) String refreshToken,
            HttpServletRequest httpRequest) {
        if (refreshToken == null) {
            throw new InvalidTokenException("Aucun refresh token fourni");
        }
        AuthResponse response = authService.refresh(refreshToken, httpRequest);
        return withRefreshCookie(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = COOKIE_NAME, required = false) String refreshToken) {
        if (refreshToken != null) {
            authService.logout(refreshToken);
        }
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, expiredCookie().toString())
                .build();
    }

    // ── Helpers ──────────────────────────────────────────────

    private ResponseEntity<AuthResponse> withRefreshCookie(AuthResponse response, HttpStatus status) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, response.refreshToken())
                .httpOnly(true)
                .secure(true) // localhost est traité comme "contexte sécurisé" par les navigateurs, donc OK en dev http://localhost
                .sameSite("Strict")
                .path("/api/auth") // le cookie ne part que vers /api/auth/**, jamais vers /api/lists/**
                .maxAge(Duration.ofMillis(jwtProperties.getRefreshTokenExpiration()))
                .build();

        // Le refresh token ne doit JAMAIS apparaître dans le corps JSON désormais
        AuthResponse body = new AuthResponse(
                response.accessToken(), null, response.tokenType(),
                response.userId(), response.email(), response.pseudo(), response.roles()
        );

        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(body);
    }

    private ResponseCookie expiredCookie() {
        return ResponseCookie.from(COOKIE_NAME, "")
                .httpOnly(true).secure(true).sameSite("Strict").path("/api/auth").maxAge(0).build();
    }
}
