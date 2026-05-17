package com.github.diegogrlima.reservaai.dto.response;

public record BookingResponseDTO(
        Long id,
        Long userId,
        String userName,
        Long roomId,
        String roomNumber,
        String status
) {
}
