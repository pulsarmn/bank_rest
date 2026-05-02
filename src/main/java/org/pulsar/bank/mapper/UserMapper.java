package org.pulsar.bank.mapper;

import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.RegistrationRequest;
import org.pulsar.bank.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User mapToUser(RegistrationRequest registrationRequest) {
        String encodedPassword = passwordEncoder.encode(registrationRequest.password());
        return User.builder()
                .login(registrationRequest.login())
                .passwordHash(encodedPassword)
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .build();
    }
}
