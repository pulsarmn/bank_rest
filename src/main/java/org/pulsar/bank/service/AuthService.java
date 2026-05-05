package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.AuthRequest;
import org.pulsar.bank.security.JwtClaims;
import org.pulsar.bank.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void authenticate(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        JwtClaims claims = JwtClaims.builder()
                .subject(authentication.getName())
                .build();
        String accessToken = jwtService.generateAccessToken(claims);
        String rawRefreshToken = jwtService.generateRefreshToken(claims);
        System.out.println();
        // TODO: generate JWT pair
    }
}
