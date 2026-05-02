package org.pulsar.bank.service;


import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.RegistrationRequest;
import org.pulsar.bank.entity.User;
import org.pulsar.bank.mapper.UserMapper;
import org.pulsar.bank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public void register(RegistrationRequest registrationRequest) {
        User user = userMapper.mapToUser(registrationRequest);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            // TODO: exception handling
        }
    }
}
