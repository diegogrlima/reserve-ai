package com.github.diegogrlima.reservaai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Dados para cadastro de reserva.")
public record CreateBookingRequestDTO(

        @Schema(description = "Identificador do usuario.", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Identificador do quarto.", example = "10")
        @NotNull
        Long roomId,

        @Schema(description = "Data de entrada da reserva (check-in). Formato: yyyy-MM-dd.", example = "2026-05-21")
        @NotNull(message = "check-in e obrigatorio")
        @FutureOrPresent(message = "check-in nao pode ser uma data no passado")
        LocalDate checkIn,

        @Schema(description = "Data de saida da reserva (check-out). Formato: yyyy-MM-dd.", example = "2026-05-25")
        @NotNull(message = "check-out e obrigatorio")
        @FutureOrPresent(message = "check-out nao pode ser uma data no passado")
        LocalDate checkOut
) {
    @AssertTrue(message = "check-out deve ser posterior ao check-in")
    public boolean isCheckOutAfterCheckIn() {
        if (checkIn == null || checkOut == null) {
            return true;
        }

        return checkOut.isAfter(checkIn);
    }
}
