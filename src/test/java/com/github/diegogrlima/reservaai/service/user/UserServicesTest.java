package com.github.diegogrlima.reservaai.service.user;

import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.exception.EmailAlreadyExistsException;
import com.github.diegogrlima.reservaai.mapper.UserMapper;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserService createUserService;

    @InjectMocks
    private GetAllUsersService getAllUsersService;

    @Test
    void createShouldBlockDuplicateEmail() {
        CreateUserRequestDTO request = new CreateUserRequestDTO("Diego", "diego@email.com", "Senha123");

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThatThrownBy(() -> createUserService.execute(request))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void createShouldEncodePasswordAndSaveUser() {
        CreateUserRequestDTO request = new CreateUserRequestDTO("Diego", "diego@email.com", "Senha123");
        User user = new User();
        User savedUser = new User();
        UserResponseDTO response = new UserResponseDTO(1L, request.name(), request.email(), "USER");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(response);

        UserResponseDTO result = createUserService.execute(request);

        assertThat(user.getPassword()).isEqualTo("encoded");
        assertThat(result).isSameAs(response);
        verify(userRepository).save(user);
    }

    @Test
    void getAllShouldMapUsersToResponsePage() {
        User user = new User();
        UserResponseDTO response = new UserResponseDTO(1L, "Diego", "diego@email.com", "USER");

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toResponse(user)).thenReturn(response);

        Page<UserResponseDTO> result = getAllUsersService.execute(PageRequest.of(0, 20));

        assertThat(result.getContent()).containsExactly(response);
    }
}
