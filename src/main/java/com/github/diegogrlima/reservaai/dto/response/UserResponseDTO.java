package com.github.diegogrlima.reservaai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representacao de usuario retornada pela API.")
public record UserResponseDTO(
        @Schema(description = "Identificador do usuario.", example = "1")
        Long id,
        @Schema(description = "Nome do usuario.", example = "Diego Lima")
        String name,
        @Schema(description = "Email do usuario.", example = "diego@email.com")
        String email,
        @Schema(description = "Perfil do usuario.", example = "USER")
        String role
) {
}
