package org.pulsar.bank.auth.mapper;

import org.pulsar.bank.auth.dto.request.RegistrationRequest;
import org.pulsar.bank.auth.domain.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {

    // TODO: maybe make this method static
    public User mapToUser(RegistrationRequest registrationRequest) {
        return User.builder()
                .login(registrationRequest.login())
                .firstName(registrationRequest.firstName())
                .lastName(registrationRequest.lastName())
                .build();
    }
}
