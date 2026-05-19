package com.github.diegogrlima.reservaai.service.auth;

import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.LoginRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.AuthResponseDTO;
import com.github.diegogrlima.reservaai.exception.InvalidCredentialsException;
import com.github.diegogrlima.reservaai.mapper.UserMapper;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return new AuthResponseDTO(jwtService.generateToken(user), "Bearer", userMapper.toResponse(user));
    }
}
