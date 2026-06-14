package sn.thiordev221.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.dto.requests.LoginRequest;
import sn.thiordev221.app.dto.requests.RefreshRequest;
import sn.thiordev221.app.dto.requests.RegisterRequest;
import sn.thiordev221.app.dto.responses.AuthResponse;
import sn.thiordev221.app.service.implementations.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest user, HttpServletRequest httpRequest) {
        AuthResponse response = authService.register(user, httpRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @RequestBody @Valid LoginRequest user,
        HttpServletRequest request
    ){
        AuthResponse response =  authService.login(user, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody @Valid RefreshRequest request,
            HttpServletRequest httpRequest
        ) {
        return ResponseEntity.ok(
            authService.refresh(request.refreshToken(), httpRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid RefreshRequest request) {
        authService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
