package sn.thiordev221.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix="app.jwt")
@Component
@Data
public class JwtProperties {
    
    private String keystorePath;
    private String keystorePassword;
    private String keyAlias;
    private String keyPassword;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;
}
