package com.github.diegogrlima.reservaai.controller;

import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.service.user.GetAllUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "Operacoes de consulta de usuarios.")
public class UserController {

    private final GetAllUsersService getAllUsersService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuarios", description = "Retorna a listagem paginada de usuarios.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios retornada com sucesso")
    })
    public ResponseEntity<Page<UserResponseDTO>> getAll(@ParameterObject Pageable pageable) {
        log.debug("Recebida requisicao para listar usuarios page={} size={} sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<UserResponseDTO> response = getAllUsersService.execute(pageable);

        return ResponseEntity.ok(response);
    }
}
