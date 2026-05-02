package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.RegistrationRequest;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegistrationRequest registrationRequest) {
        User user = mapToUser(registrationRequest);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            // TODO: exception handling
        }
    }

    private User mapToUser(RegistrationRequest registrationRequest) {
        String encodedPassword = passwordEncoder.encode(registrationRequest.password());
        return User.builder()
                .login(registrationRequest.login())
                .passwordHash(encodedPassword)
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .build();
    }
}
