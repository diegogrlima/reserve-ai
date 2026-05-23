package com.github.diegogrlima.reservaai.controller;

import com.github.diegogrlima.reservaai.dto.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.LoginRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.AuthResponseDTO;
import com.github.diegogrlima.reservaai.dto.response.ErrorResponseDTO;
import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.service.auth.AuthService;
import com.github.diegogrlima.reservaai.service.user.CreateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticacao", description = "Operacoes de autenticacao da API.")
public class AuthController {

    private final AuthService authService;
    private final CreateUserService createUserService;

    @PostMapping("/register")
    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário com e-mail único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody CreateUserRequestDTO request) {
        log.info("Recebida requisicao para cadastrar usuario email={}", request.email());
        UserResponseDTO response = createUserService.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Valida e-mail e senha e retorna um token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario autenticado com sucesso",
                    content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validacao",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais invalidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("Recebida requisicao de login email={}", request.email());
        return ResponseEntity.ok(authService.login(request));
    }
}
