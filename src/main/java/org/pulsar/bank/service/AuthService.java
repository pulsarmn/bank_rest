package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.AuthRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    public void authenticate(AuthRequest authRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.login(), authRequest.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // TODO: generate JWT pair
    }
}
