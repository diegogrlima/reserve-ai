package com.github.diegogrlima.reservaai.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateBookingRequestDTO(
        @NotNull
        Long userId,

        @NotNull
        Long roomId
) {
}
