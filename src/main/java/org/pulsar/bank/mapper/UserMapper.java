package org.pulsar.bank.mapper;

import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.RegistrationRequest;
import org.pulsar.bank.entity.User;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    public User mapToUser(RegistrationRequest registrationRequest) {
        return User.builder()
                .login(registrationRequest.login())
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .build();
    }
}
