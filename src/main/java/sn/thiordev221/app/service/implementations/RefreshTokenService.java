package sn.thiordev221.app.service.implementations;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.config.JwtProperties;
import sn.thiordev221.app.custom_exceptons.InvalidTokenException;
import sn.thiordev221.app.custom_exceptons.TokenReplayException;
import sn.thiordev221.app.model.RefreshToken;
import sn.thiordev221.app.model.Utilisateur;
import sn.thiordev221.app.repository.RefreshTokenRepository;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties props;
    
    public RefreshToken generateRefreshToken(Utilisateur utilisateur, HttpServletRequest request){

        RefreshToken token = new RefreshToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUtilisateur(utilisateur);
        token.setExpiresAt(
            Instant.now().plusMillis(props.getRefreshTokenExpiration())
        );
        token.setDeviceInfo(request.getHeader("User-Agent"));
        token.setIpAddress(request.getRemoteAddr());

        return refreshTokenRepository.save(token);
    }

    public RefreshToken validateAndRotate(String tokenValue){
        RefreshToken token = refreshTokenRepository.findById(tokenValue)
                            .orElseThrow(()->new InvalidTokenException("Token Inconnu !"));

        if(!token.isValid()){

            if(token.getRevokedAt() != null){

                revokeAllUserTokens(token.getUtilisateur());
                throw new TokenReplayException(
                    "Refresh token déjà utilisé — tous les tokens révoqués");
            }
        }

        token.setRevokedAt(Instant.now());
        refreshTokenRepository.save(token);
        return token;
    }

    public void revokeAllUserTokens(Utilisateur utilisateur){
        refreshTokenRepository.revokeAllByUser(utilisateur, Instant.now());
    }

}
