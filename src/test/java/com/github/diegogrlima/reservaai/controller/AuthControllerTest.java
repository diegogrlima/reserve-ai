package com.github.diegogrlima.reservaai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.diegogrlima.reservaai.dto.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.LoginRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.AuthResponseDTO;
import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.service.auth.AuthService;
import com.github.diegogrlima.reservaai.service.user.CreateUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private final AuthService authService = mock(AuthService.class);
    private final CreateUserService createUserService = mock(CreateUserService.class);

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        AuthController authController = new AuthController(authService, createUserService);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setValidator(validator)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void registerShouldReturnCreatedWhenPayloadIsValid() throws Exception {
        CreateUserRequestDTO request = new CreateUserRequestDTO("Diego Lima", "diego@email.com", "Senha123");
        UserResponseDTO response = new UserResponseDTO(1L, request.name(), request.email(), "USER");

        when(createUserService.execute(any(CreateUserRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(request.email()));

        verify(createUserService).execute(any(CreateUserRequestDTO.class));
    }

    @Test
    void registerShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        CreateUserRequestDTO request = new CreateUserRequestDTO("", "invalid-email", "123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(createUserService, never()).execute(any(CreateUserRequestDTO.class));
    }

    @Test
    void loginShouldReturnOkWhenPayloadIsValid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("diego@email.com", "Senha123");
        UserResponseDTO user = new UserResponseDTO(1L, "Diego Lima", request.email(), "USER");
        AuthResponseDTO response = new AuthResponseDTO("token", "Bearer", user);

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.type").value("Bearer"));

        verify(authService).login(any(LoginRequestDTO.class));
    }

    @Test
    void loginShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("invalid-email", "");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any(LoginRequestDTO.class));
    }
}
