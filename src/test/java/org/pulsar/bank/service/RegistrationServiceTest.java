package org.pulsar.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pulsar.bank.auth.service.RegistrationService;
import org.pulsar.bank.auth.dto.request.RegistrationRequest;
import org.pulsar.bank.auth.domain.User;
import org.pulsar.bank.auth.exception.UserAlreadyExistsException;
import org.pulsar.bank.auth.mapper.UserMapper;
import org.pulsar.bank.auth.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

// TODO: extract duplicate data
@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    private static final String HASHED_PASSWORD = "hashed_password";

    @Test
    void shouldRegisterNewUser_whenValidRegistrationRequest() {
        RegistrationRequest correctRegistrationRequest = RegistrationRequest.builder()
                .login("correct_login")
                .password("correct_password")
                .firstName("first_name")
                .lastName("last_name")
                .build();

        User user = User.builder()
                .login("correct_login")
                .firstName("first_name")
                .lastName("last_name")
                .build();

        doReturn(user).when(userMapper).mapToUser(correctRegistrationRequest);
        doReturn(HASHED_PASSWORD).when(passwordEncoder).encode(correctRegistrationRequest.password());

        registrationService.register(correctRegistrationRequest);
        verify(userRepository, Mockito.times(1)).save(user);
        verify(userRepository, Mockito.times(1)).flush();
    }

    @Test
    void shouldThrowsAnException_whenNullRegistrationRequest() {
        RegistrationRequest invalidRegistrationRequest = null;

        doThrow(NullPointerException.class).when(userMapper).mapToUser(invalidRegistrationRequest);

        assertThatThrownBy(() -> registrationService.register(invalidRegistrationRequest))
                .isInstanceOf(NullPointerException.class);
        verify(userMapper, times(1)).mapToUser(invalidRegistrationRequest);
    }

    @Test
    void shouldThrowsAnException_whenUserWithThisLoginAlreadyExists() {
        RegistrationRequest invalidRegistrationRequest = RegistrationRequest.builder()
                .login("existing_login")
                .password("correct_password")
                .firstName("Alex")
                .lastName("Gray")
                .build();

        User user = User.builder()
                .login(invalidRegistrationRequest.login())
                .firstName(invalidRegistrationRequest.firstName())
                .lastName(invalidRegistrationRequest.lastName())
                .build();

        doReturn(user).when(userMapper).mapToUser(invalidRegistrationRequest);
        doReturn(HASHED_PASSWORD).when(passwordEncoder).encode(invalidRegistrationRequest.password());
        doThrow(DataIntegrityViolationException.class).when(userRepository).flush();

        assertThatThrownBy(() -> registrationService.register(invalidRegistrationRequest))
                .isInstanceOf(UserAlreadyExistsException.class);
        verify(userRepository, times(1)).save(user);
    }
}
