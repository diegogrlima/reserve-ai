package com.github.diegogrlima.reservaai.dto.response;

import java.math.BigDecimal;

public record RoomResponseDTO(
        Long id,
        String roomNumber,
        String roomType,
        BigDecimal dailyRate
) {
}
