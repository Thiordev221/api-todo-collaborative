package sn.thiordev221.app.service.implementations;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.custom_exceptons.UtilisateurConflictException;
import sn.thiordev221.app.custom_exceptons.UtilisateurNotFoundException;
import sn.thiordev221.app.dto.requests.LoginRequest;
import sn.thiordev221.app.dto.requests.RegisterRequest;
import sn.thiordev221.app.dto.responses.AuthResponse;
import sn.thiordev221.app.mapper.AuthMapper;
import sn.thiordev221.app.model.RefreshToken;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.RefreshTokenRepository;
import sn.thiordev221.app.repository.UtilisateurRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final RefreshTokenService refreshTokenService;
    private final UtilisateurRepository utilisateurRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthMapper mapper;
    private final PasswordEncoder encoder;

    public AuthResponse register(RegisterRequest request,
                                 HttpServletRequest httpRequest) {

        // 1. Vérifier l'unicité
        if (utilisateurRepository.existsByEmail(request.email())) {
            throw new UtilisateurConflictException(
                "Un compte existe déjà avec cet email");
        }

        if (utilisateurRepository.existsByPseudo(request.pseudo())) {
            throw new UtilisateurConflictException(
                "Ce pseudo est déjà utilisé");
        }

        String password = encoder.encode(request.password());
        Utilisateur user = mapper.toUtilisateurFromRegisterRequest(request);
        user.setPassword(password);

        Utilisateur saved = utilisateurRepository.saveAndFlush(user);
        return buildAuthResponse(saved, httpRequest);
    }
    public AuthResponse login(LoginRequest user, HttpServletRequest request){

        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                user.email(),
                user.password()
            )
        );

        Utilisateur found = utilisateurRepository.findByEmail(user.email())
                            .orElseThrow(()->new UtilisateurNotFoundException("Utilisateur", "email", user.email()));

        return buildAuthResponse(found, request);
    }

     public AuthResponse refresh(String refreshTokenValue,
                                HttpServletRequest httpRequest) {
        // 1. Valide + révoque l'ancien refresh token (rotation)
        RefreshToken old = refreshTokenService.validateAndRotate(refreshTokenValue);
        Utilisateur user = old.getUtilisateur();

        // 2. Nouveau access token
        String newAccessToken = jwtService.generetaAccessToken(user);

        // 3. Nouveau refresh token
        RefreshToken newRefreshToken = refreshTokenService
                .generateRefreshToken(user, httpRequest);
            
        String refresh = newRefreshToken.getToken();

        List<String> roles = user.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .toList();

        return new AuthResponse(newAccessToken, refresh, user.getId(), user.getEmail(), user.getUsername(), roles);
    }

    public void logout(String refreshTokenValue) {
        RefreshToken token = refreshTokenRepository
                .findById(refreshTokenValue)
                .orElseThrow();
        refreshTokenService.revokeAllUserTokens(token.getUtilisateur());
    }
        // ── MÉTHODE COMMUNE ──────────────────────────────────────

    private AuthResponse buildAuthResponse(Utilisateur user,
                                           HttpServletRequest httpRequest) {
        // Révoque les anciens refresh tokens
        refreshTokenService.revokeAllUserTokens(user);

        String accessToken = jwtService.generetaAccessToken(user);
        RefreshToken refreshToken = refreshTokenService
                .generateRefreshToken(user, httpRequest);

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new AuthResponse(
            accessToken,
            refreshToken.getToken(),
            user.getId(),
            user.getEmail(),
            user.getPseudo(),
            roles
        );
    }
}


