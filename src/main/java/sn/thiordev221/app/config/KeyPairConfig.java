package sn.thiordev221.app.config;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class KeyPairConfig {

    private final JwtProperties props;

    @Bean
    public KeyPair keyPair() throws Exception{
        
        Resource resource = new DefaultResourceLoader()
                                .getResource(props.getKeystorePath());

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(
            resource.getInputStream(),
            props.getKeystorePassword().toCharArray()
        );

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(props.getKeyAlias(), props.getKeyPassword().toCharArray());

        PublicKey publicKey = keyStore.getCertificate(props.getKeyAlias()).getPublicKey();

        return new KeyPair(publicKey, privateKey);
    }
    
}
