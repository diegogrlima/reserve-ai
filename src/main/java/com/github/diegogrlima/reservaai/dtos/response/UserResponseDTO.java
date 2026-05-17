package com.github.diegogrlima.reservaai.dtos.response;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String role
) {
}
