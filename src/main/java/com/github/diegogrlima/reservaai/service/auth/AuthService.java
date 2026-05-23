package com.github.diegogrlima.reservaai.service.auth;

import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.LoginRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.AuthResponseDTO;
import com.github.diegogrlima.reservaai.exception.InvalidCredentialsException;
import com.github.diegogrlima.reservaai.mapper.UserMapper;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthResponseDTO login(LoginRequestDTO request) {
        log.debug("Iniciando autenticacao email={}", request.email());

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("Autenticacao bloqueada: usuario nao encontrado email={}", request.email());
                    return new InvalidCredentialsException();
                });

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            log.warn("Autenticacao bloqueada: senha invalida email={}", request.email());
            throw new InvalidCredentialsException();
        }

        log.info("Usuario autenticado com sucesso id={} email={}", user.getId(), user.getEmail());

        return new AuthResponseDTO(jwtService.generateToken(user), "Bearer", userMapper.toResponse(user));
    }
}
