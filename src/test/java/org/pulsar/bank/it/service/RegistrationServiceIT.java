package org.pulsar.bank.it.service;

import org.junit.jupiter.api.Test;
import org.pulsar.bank.auth.dto.request.RegistrationRequest;
import org.pulsar.bank.auth.domain.Role;
import org.pulsar.bank.auth.domain.User;
import org.pulsar.bank.auth.exception.UserAlreadyExistsException;
import org.pulsar.bank.auth.repository.UserRepository;
import org.pulsar.bank.auth.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Testcontainers
@SpringBootTest
@Transactional
class RegistrationServiceIT {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17");

    @Test
    void shouldRegisterNewUser_whenValidRegistrationRequest() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .login("correct_login")
                .password("correct_password")
                .firstName("Alex")
                .lastName("Gray")
                .build();

        registrationService.register(registrationRequest);
        Optional<User> user = userRepository.findByLogin(registrationRequest.login());

        assertThat(user).isPresent();
        User savedUser = user.get();

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getLogin()).isEqualTo(registrationRequest.login());
        assertThat(savedUser.getPasswordHash()).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);
        assertThat(savedUser.getFirstName()).isEqualTo(registrationRequest.firstName());
        assertThat(savedUser.getLastName()).isEqualTo(registrationRequest.lastName());
    }

    @Test
    void shouldThrowAnException_whenUserAlreadyExists() {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .login("correct_login")
                .password("correct_password")
                .firstName("Alex")
                .lastName("Gray")
                .build();

        registrationService.register(registrationRequest);

        assertThatThrownBy(() -> registrationService.register(registrationRequest))
                .isInstanceOf(UserAlreadyExistsException.class);
    }
}
