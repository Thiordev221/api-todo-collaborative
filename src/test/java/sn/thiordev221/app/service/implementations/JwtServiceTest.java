package sn.thiordev221.app.service.implementations;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import sn.thiordev221.app.config.JwtProperties;

class JwtServiceTest {

    @Test
    void generateAndValidateToken() throws Exception {
        JwtProperties props = new JwtProperties();
        props.setAccessTokenExpiration(60_000);

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();

        JwtService jwtService = new JwtService(props, kp);

        UserDetails user = User.withUsername("user@example.com").password("pwd").roles("USER").build();

        String token = jwtService.generetaAccessToken(user);

        assertThat(token).isNotNull();
        assertThat(jwtService.extractUsername(token)).isEqualTo(user.getUsername());
        assertThat(jwtService.isTokenValid(token, user)).isTrue();
    }
}
