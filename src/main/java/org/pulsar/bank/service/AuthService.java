package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.config.properties.JwtProperties;
import org.pulsar.bank.dto.AuthRequest;
import org.pulsar.bank.dto.AuthResponse;
import org.pulsar.bank.entity.RefreshToken;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.repository.RefreshTokenRepository;
import org.pulsar.bank.repository.UserRepository;
import org.pulsar.bank.security.JwtClaims;
import org.pulsar.bank.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtProperties jwtProperties;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponse authenticate(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        JwtClaims claims = JwtClaims.builder()
                .subject(authentication.getName())
                .build();
        String accessToken = jwtService.generateAccessToken(claims);
        String rawRefreshToken = jwtService.generateRefreshToken(claims);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(rawRefreshToken.getBytes(StandardCharsets.UTF_8));
            String refreshTokenHash = Base64.getEncoder().encodeToString(hashBytes);

            User user = userRepository.findByLogin(authRequest.login()).get();

            Instant expirationTime = Instant.now().plusMillis(jwtProperties.getRefreshToken().getExpirationMillis());

            RefreshToken refreshToken = RefreshToken.builder()
                    .tokenHash(refreshTokenHash)
                    .user(user)
                    .expiresAt(expirationTime)
                    .build();
            refreshTokenRepository.save(refreshToken);

            return new AuthResponse(accessToken, rawRefreshToken);
        } catch (NoSuchAlgorithmException e) {
            // shouldn't happen
            throw new RuntimeException(e);
        }
    }
}
