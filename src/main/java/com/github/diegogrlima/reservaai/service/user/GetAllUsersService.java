package com.github.diegogrlima.reservaai.service.user;

import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.mapper.UserMapper;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllUsersService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> execute(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }
}
