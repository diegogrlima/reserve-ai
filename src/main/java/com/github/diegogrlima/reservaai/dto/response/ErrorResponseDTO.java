package com.github.diegogrlima.reservaai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Resposta padrao de erro da API.")
public record ErrorResponseDTO(
        @Schema(description = "Momento em que o erro ocorreu.", example = "2026-05-17T12:00:00Z")
        Instant timestamp,
        @Schema(description = "Codigo HTTP retornado.", example = "409")
        int status,
        @Schema(description = "Descricao do status HTTP.", example = "Conflict")
        String error,
        @Schema(description = "Mensagem detalhada do erro.", example = "Room already has a booking: 10")
        String message,
        @Schema(description = "Caminho da requisicao que gerou o erro.", example = "/rooms/10")
        String path
) {
}
