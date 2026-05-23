package com.github.diegogrlima.reservaai.service.user;

import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.exception.EmailAlreadyExistsException;
import com.github.diegogrlima.reservaai.mapper.UserMapper;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO execute(CreateUserRequestDTO request) {
        log.debug("Iniciando cadastro de usuario email={}", request.email());

        if (userRepository.existsByEmail(request.email())) {
            log.warn("Cadastro de usuario bloqueado por email ja existente email={}", request.email());
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);

        log.info("Usuario cadastrado com sucesso id={} email={}", savedUser.getId(), savedUser.getEmail());

        return userMapper.toResponse(savedUser);
    }
}
