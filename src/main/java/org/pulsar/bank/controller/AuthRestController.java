package org.pulsar.bank.controller;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.RegistrationRequest;
import org.pulsar.bank.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegistrationRequest registrationRequest) {
        registrationService.register(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
