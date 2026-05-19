package com.github.diegogrlima.reservaai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta da autenticacao.")
public record AuthResponseDTO(
        @Schema(description = "Token JWT para uso no header Authorization.", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,
        @Schema(description = "Tipo do token.", example = "Bearer")
        String type,
        @Schema(description = "Usuario autenticado.")
        UserResponseDTO user
) {
}
