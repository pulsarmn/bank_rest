package org.pulsar.bank.mapper;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.pulsar.bank.dto.RegistrationRequest;
import org.pulsar.bank.entity.Role;
import org.pulsar.bank.entity.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeAll
    void beforeAll() {
        userMapper = new UserMapper();
    }

    @Test
    void shouldMapAllFieldsCorrectly_whenValidRequest() {
        RegistrationRequest registrationRequest = new RegistrationRequest("correct_login", "correct_password", "Alex", "Gray");

        User user = userMapper.mapToUser(registrationRequest);

        assertThat(user.getLogin()).isEqualTo("correct_login");
        assertThat(user.getFirstName()).isEqualTo("Alex");
        assertThat(user.getLastName()).isEqualTo("Gray");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getPasswordHash()).isNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getCreatedAt()).isNull();
    }

    @Test
    void shouldThrowAnException_whenNullArgument() {
        RegistrationRequest registrationRequest = null;

        assertThatThrownBy(() -> userMapper.mapToUser(registrationRequest))
                .isInstanceOf(NullPointerException.class);
    }
}
