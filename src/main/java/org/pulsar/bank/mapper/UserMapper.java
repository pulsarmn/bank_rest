package org.pulsar.bank.mapper;

import lombok.RequiredArgsConstructor;
import org.pulsar.bank.dto.RegistrationRequest;
import org.pulsar.bank.entity.User;
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
