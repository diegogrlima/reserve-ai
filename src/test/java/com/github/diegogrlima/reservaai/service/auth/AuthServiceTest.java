package com.github.diegogrlima.reservaai.service.auth;

import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.LoginRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.AuthResponseDTO;
import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.exception.InvalidCredentialsException;
import com.github.diegogrlima.reservaai.mapper.UserMapper;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginShouldReturnTokenWhenCredentialsAreValid() {
        LoginRequestDTO request = new LoginRequestDTO("diego@email.com", "Senha123");
        User user = new User();
        user.setEmail(request.email());
        user.setPassword("encoded");
        UserResponseDTO userResponse = new UserResponseDTO(1L, "Diego", request.email(), "USER");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token");
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        AuthResponseDTO response = authService.login(request);

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.type()).isEqualTo("Bearer");
        assertThat(response.user()).isSameAs(userResponse);
    }

    @Test
    void loginShouldThrowWhenUserDoesNotExist() {
        LoginRequestDTO request = new LoginRequestDTO("diego@email.com", "Senha123");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(passwordEncoder, never()).matches(request.password(), "encoded");
    }

    @Test
    void loginShouldThrowWhenPasswordIsInvalid() {
        LoginRequestDTO request = new LoginRequestDTO("diego@email.com", "Senha123");
        User user = new User();
        user.setEmail(request.email());
        user.setPassword("encoded");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(jwtService, never()).generateToken(user);
    }
}
