package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.config.properties.JwtProperties;
import org.pulsar.bank.dto.request.AuthRequest;
import org.pulsar.bank.dto.response.AuthResponse;
import org.pulsar.bank.entity.RefreshToken;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.security.jwt.JwtClaims;
import org.pulsar.bank.security.jwt.JwtService;
import org.pulsar.bank.security.SecurityUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse authenticate(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        JwtClaims claims = buildClaims(authentication);
        String accessToken = jwtService.generateAccessToken(claims);
        String rawRefreshToken = jwtService.generateRefreshToken();

        User user = ((SecurityUser) authentication.getPrincipal()).getUser();
        RefreshToken refreshToken = refreshTokenService.create(user, rawRefreshToken);
        return new AuthResponse(accessToken, rawRefreshToken, refreshToken.getId().toString());
    }

    private JwtClaims buildClaims(Authentication authentication) {
        List<String> roles = getRoles(authentication);

        return JwtClaims.builder()
                .subject(authentication.getName())
                .claim("iat", Instant.now().getEpochSecond())
                .claim("exp", Instant.now().plusMillis(jwtProperties.getAccessToken().getExpirationMillis()).getEpochSecond())
                .claim("roles", roles)
                .build();
    }

    private List<String> getRoles(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .map(authority -> authority.startsWith("ROLE_") ? authority.substring(5) : authority)
                .collect(Collectors.toList());
    }
}
