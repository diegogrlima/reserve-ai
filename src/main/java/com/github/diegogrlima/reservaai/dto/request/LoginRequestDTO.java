package com.github.diegogrlima.reservaai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dados para autenticacao.")
public record LoginRequestDTO(
        @Schema(description = "Email do usuario.", example = "diego@email.com")
        @Email
        @NotBlank
        String email,

        @Schema(description = "Senha do usuario.", example = "Senha@123")
        @NotBlank
        String password
) {
}
