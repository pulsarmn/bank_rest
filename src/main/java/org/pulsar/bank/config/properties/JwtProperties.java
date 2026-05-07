package org.pulsar.bank.config.properties;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Data
@Component
@Validated
@ConfigurationProperties("app.jwt")
public class JwtProperties {

    private AccessToken accessToken = new AccessToken();
    private RefreshToken refreshToken = new RefreshToken();

    /**
     * Access Token configuration properties
     */
    @Data
    public static class AccessToken {

        /**
         * The private key for signing access tokens
         */
        @NotBlank
        private String privateKey;

        /**
         * The public key for verifying access tokens
         */
        @NotBlank
        private String publicKey;

        /**
         * Lifetime of access token in milliseconds
         */
        @Min(300_000) // 5 minutes
        @Max(1_800_000) // 30 minutes
        private long expirationMillis = 300_000;
    }

    /**
     * Refresh Token configuration properties
     */
    @Data
    public static class RefreshToken {

        /**
         * Lifetime of refresh token in milliseconds
         */
        @Min(86400000)
        private long expirationMillis = 604_800_000;
    }
}
