package com.github.diegogrlima.reservaai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateRoomRequestDTO(
        @NotBlank
        @Size(max = 15)
        String roomNumber,

        @NotBlank
        @Size(max = 20)
        String roomType,

        @NotNull
        @Positive
        BigDecimal dailyRate
) {
}
