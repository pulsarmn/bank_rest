package org.pulsar.bank.config.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties("app.jwt")
public class JwtProperties {

    private AccessToken accessToken;
    private RefreshToken refreshToken;

    @Data
    public static class AccessToken {

        private String privateKey;
        private long expirationMillis = 300_000;
    }

    @Data
    public static class RefreshToken {

        private long expirationMillis = 604_800_000;
    }
}
