package com.github.diegogrlima.reservaai.dto.response;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String role
) {
}
