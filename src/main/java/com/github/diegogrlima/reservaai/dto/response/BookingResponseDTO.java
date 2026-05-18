package com.github.diegogrlima.reservaai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representacao de reserva retornada pela API.")
public record BookingResponseDTO(
        @Schema(description = "Identificador da reserva.", example = "1")
        Long id,
        @Schema(description = "Identificador do usuario.", example = "1")
        Long userId,
        @Schema(description = "Nome do usuario.", example = "Diego Lima")
        String userName,
        @Schema(description = "Identificador do quarto.", example = "10")
        Long roomId,
        @Schema(description = "Numero do quarto.", example = "101")
        String roomNumber,
        @Schema(description = "Status atual da reserva.", example = "CONFIRMED")
        String status
) {
}
