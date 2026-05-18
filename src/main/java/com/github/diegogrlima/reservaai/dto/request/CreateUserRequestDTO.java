package com.github.diegogrlima.reservaai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para cadastro de usuario.")
public record CreateUserRequestDTO(
        @Schema(description = "Nome do usuario.", example = "Diego Lima")
        @NotBlank
        @Size(max = 100)
        String name,

        @Schema(description = "Email unico do usuario.", example = "diego@email.com")
        @Email
        @NotBlank
        @Size(max = 150)
        String email
) {
}
