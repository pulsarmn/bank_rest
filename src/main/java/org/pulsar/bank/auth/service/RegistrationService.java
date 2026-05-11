package org.pulsar.bank.auth.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.auth.domain.User;
import org.pulsar.bank.auth.dto.request.RegistrationRequest;
import org.pulsar.bank.auth.exception.UserAlreadyExistsException;
import org.pulsar.bank.auth.mapper.UserMapper;
import org.pulsar.bank.auth.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegistrationRequest registrationRequest) {
        User user = userMapper.mapToUser(registrationRequest);
        String passwordHash = passwordEncoder.encode(registrationRequest.password());
        user.setPasswordHash(passwordHash);

        try {
            userRepository.save(user);
            userRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("The user with username '%s' already exists".formatted(registrationRequest.login()));
        }
    }
}
