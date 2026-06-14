package sn.thiordev221.app.service.implementations;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import sn.thiordev221.app.config.JwtProperties;

@Service
@RequiredArgsConstructor
public class JwtService {
    
    private final JwtProperties props;
    private final KeyPair jwtKeyPair;

    public String generetaAccessToken(UserDetails userDetails){
        return Jwts.builder()
                    .subject(userDetails.getUsername())
                    .issuer("auth-service")
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + props.getAccessTokenExpiration()))
                    .claim("roles", extractRoles(userDetails))
                    .claim("type", "access")
                    .signWith(jwtKeyPair.getPrivate(), Jwts.SIG.RS256)
                    .compact();
    } 

    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = Jwts.parser()
                        .verifyWith((RSAPublicKey) jwtKeyPair.getPublic())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
        
        return resolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        try{
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);

        }catch(JwtException ex){
            return false;
        }
    }

    public boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    private List<String> extractRoles(UserDetails user){
        return user.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .toList();
    }
}
