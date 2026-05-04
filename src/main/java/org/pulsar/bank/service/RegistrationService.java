package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.RegistrationRequest;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.exception.UserAlreadyExistsException;
import org.pulsar.bank.mapper.UserMapper;
import org.pulsar.bank.repository.UserRepository;
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
