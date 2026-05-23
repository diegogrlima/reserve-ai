package com.github.diegogrlima.reservaai.service.auth;

import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Carregando usuario para autenticacao username={}", username);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    log.warn("Usuario nao encontrado para autenticacao username={}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        log.debug("Usuario carregado para autenticacao userId={} username={}", user.getId(), username);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
