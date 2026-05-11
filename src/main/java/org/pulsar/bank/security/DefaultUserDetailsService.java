package org.pulsar.bank.security;

import lombok.RequiredArgsConstructor;
import org.pulsar.bank.auth.domain.User;
import org.pulsar.bank.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DefaultUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userRepository.findByLogin(login)
                .map(this::map)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private UserDetails map(User user) {
        return new SecurityUser(user);
    }
}
