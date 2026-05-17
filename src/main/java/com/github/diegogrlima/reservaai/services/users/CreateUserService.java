package com.github.diegogrlima.reservaai.services.users;

import com.github.diegogrlima.reservaai.dtos.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dtos.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.exceptions.EmailAlreadyExistsException;
import com.github.diegogrlima.reservaai.mapper.UserMapper;
import com.github.diegogrlima.reservaai.model.User;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO execute(CreateUserRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }
}
