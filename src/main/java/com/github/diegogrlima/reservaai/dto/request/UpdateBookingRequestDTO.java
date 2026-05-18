package com.github.diegogrlima.reservaai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para atualizacao de reserva.")
public record UpdateBookingRequestDTO(
        @Schema(description = "Identificador do usuario.", example = "2")
        @NotNull
        Long userId,

        @Schema(description = "Identificador do quarto.", example = "11")
        @NotNull
        Long roomId
) {
}
