package com.github.diegogrlima.reservaai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para cadastro de reserva.")
public record CreateBookingRequestDTO(
        @Schema(description = "Identificador do usuario.", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Identificador do quarto.", example = "10")
        @NotNull
        Long roomId
) {
}
