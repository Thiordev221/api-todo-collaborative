package sn.thiordev221.app.service.implementations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import sn.thiordev221.app.config.JwtProperties;
import sn.thiordev221.app.custom_exceptons.InvalidTokenException;
import sn.thiordev221.app.custom_exceptons.TokenReplayException;
import sn.thiordev221.app.model.RefreshToken;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.RefreshTokenRepository;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtProperties props;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void generateRefreshToken_savesToken() {
        Utilisateur u = Utilisateur.builder().id(1L).build();
        HttpServletRequest req = org.mockito.Mockito.mock(HttpServletRequest.class);
        when(req.getHeader("User-Agent")).thenReturn("ua");
        when(req.getRemoteAddr()).thenReturn("127.0.0.1");
        when(props.getRefreshTokenExpiration()).thenReturn(3600L);

        RefreshToken toSave = new RefreshToken();
        when(refreshTokenRepository.save(org.mockito.Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        RefreshToken rt = refreshTokenService.generateRefreshToken(u, req);

        assertThat(rt.getUtilisateur()).isEqualTo(u);
        assertThat(rt.getToken()).isNotNull();
    }

    @Test
    void validateAndRotate_invalidToken() {
        RefreshToken stored = new RefreshToken();
        stored.setToken("t1");
        stored.setUtilisateur(Utilisateur.builder().id(2L).build());
        stored.setExpiresAt(Instant.now().minusSeconds(1));
        stored.setRevokedAt(Instant.now().minusSeconds(1));

        when(refreshTokenRepository.findById("t1")).thenReturn(Optional.of(stored));
        assertThrows(TokenReplayException.class, () -> refreshTokenService.validateAndRotate("t1"));
        // verify(refreshTokenRepository).save(stored);
    }

    @Test
    void validateAndRotate_unknownToken() {
        when(refreshTokenRepository.findById("x")).thenReturn(Optional.empty());

        assertThrows(InvalidTokenException.class, () -> refreshTokenService.validateAndRotate("x"));
    }
}
