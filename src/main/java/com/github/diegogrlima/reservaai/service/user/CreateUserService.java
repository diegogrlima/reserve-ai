package com.github.diegogrlima.reservaai.service.user;

import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.exception.EmailAlreadyExistsException;
import com.github.diegogrlima.reservaai.mapper.UserMapper;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO execute(CreateUserRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }
}
