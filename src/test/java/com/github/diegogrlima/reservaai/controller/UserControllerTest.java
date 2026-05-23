package com.github.diegogrlima.reservaai.controller;

import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.service.user.GetAllUsersService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private final GetAllUsersService getAllUsersService = mock(GetAllUsersService.class);
    private final UserController userController = new UserController(getAllUsersService);

    @Test
    void getAllShouldReturnUsersPage() {
        Page<UserResponseDTO> page = new PageImpl<>(List.of(new UserResponseDTO(1L, "Diego", "diego@email.com", "USER")));

        when(getAllUsersService.execute(any())).thenReturn(page);

        ResponseEntity<Page<UserResponseDTO>> response = userController.getAll(PageRequest.of(0, 20));

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isSameAs(page);
        verify(getAllUsersService).execute(any());
    }
}
