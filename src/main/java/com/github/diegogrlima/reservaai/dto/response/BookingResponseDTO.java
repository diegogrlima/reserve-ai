package com.github.diegogrlima.reservaai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

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
        String status,
        @Schema(description = "Data de entrada da reserva.", example = "2026-05-21")
        LocalDate checkIn,
        @Schema(description = "Data de saida da reserva.", example = "2026-05-25")
        LocalDate checkOut,
        @Schema(description = "Valor total estimado da reserva.", example = "799.60")
        BigDecimal totalEstimatedValue
) {
}
