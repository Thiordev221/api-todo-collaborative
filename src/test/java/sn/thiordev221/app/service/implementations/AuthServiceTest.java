package sn.thiordev221.app.service.implementations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;
import sn.thiordev221.app.dto.requests.LoginRequest;
import sn.thiordev221.app.dto.requests.RegisterRequest;
import sn.thiordev221.app.dto.responses.AuthResponse;
import sn.thiordev221.app.mapper.AuthMapper;
import sn.thiordev221.app.model.RefreshToken;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.RefreshTokenRepository;
import sn.thiordev221.app.repository.UtilisateurRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private AuthMapper mapper;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_success() {
        RegisterRequest req = new RegisterRequest("p", "u@e.com", "Password1!");
        HttpServletRequest httpReq = org.mockito.Mockito.mock(HttpServletRequest.class);

        Utilisateur user = Utilisateur.builder().email(req.email()).pseudo(req.pseudo()).build();
        Utilisateur saved = Utilisateur.builder().id(11L).email(req.email()).pseudo(req.pseudo()).build();
        RefreshToken rt = new RefreshToken();
        rt.setToken("r1");

        when(utilisateurRepository.existsByEmail(req.email())).thenReturn(false);
        when(utilisateurRepository.existsByPseudo(req.pseudo())).thenReturn(false);
        when(mapper.toUtilisateurFromRegisterRequest(req)).thenReturn(user);
        when(encoder.encode(req.password())).thenReturn("enc");
        when(utilisateurRepository.saveAndFlush(org.mockito.Mockito.any())).thenReturn(saved);
        when(jwtService.generetaAccessToken(saved)).thenReturn("access");
        when(refreshTokenService.generateRefreshToken(saved, httpReq)).thenReturn(rt);

        AuthResponse res = authService.register(req, httpReq);

        assertThat(res.accessToken()).isEqualTo("access");
        assertThat(res.refreshToken()).isEqualTo("r1");
        verify(utilisateurRepository).saveAndFlush(org.mockito.Mockito.any());
    }

    @Test
    void login_success() {
        LoginRequest lr = new LoginRequest("u@e.com", "pwd");
        HttpServletRequest httpReq = org.mockito.Mockito.mock(HttpServletRequest.class);

        Utilisateur user = Utilisateur.builder().id(12L).email(lr.email()).pseudo("p").build();
        Authentication auth = org.mockito.Mockito.mock(Authentication.class);

        when(authManager.authenticate(org.mockito.Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(utilisateurRepository.findByEmail(lr.email())).thenReturn(Optional.of(user));
        when(jwtService.generetaAccessToken(user)).thenReturn("access2");
        RefreshToken rt = new RefreshToken(); rt.setToken("r2");
        when(refreshTokenService.generateRefreshToken(user, httpReq)).thenReturn(rt);

        var res = authService.login(lr, httpReq);

        assertThat(res.accessToken()).isEqualTo("access2");
        assertThat(res.refreshToken()).isEqualTo("r2");
    }

    @Test
    void logout_revokesTokens() {
        RefreshToken token = new RefreshToken();
        Utilisateur u = Utilisateur.builder().id(7L).build();
        token.setToken("t1");
        token.setUtilisateur(u);

        when(refreshTokenRepository.findById("t1")).thenReturn(Optional.of(token));

        authService.logout("t1");

        verify(refreshTokenService).revokeAllUserTokens(u);
    }
}
