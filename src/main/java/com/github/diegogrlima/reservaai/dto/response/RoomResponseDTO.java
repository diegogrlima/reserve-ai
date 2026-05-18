package com.github.diegogrlima.reservaai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Representacao de quarto retornada pela API.")
public record RoomResponseDTO(
        @Schema(description = "Identificador do quarto.", example = "1")
        Long id,
        @Schema(description = "Numero do quarto.", example = "101")
        String roomNumber,
        @Schema(description = "Tipo do quarto.", example = "STANDARD")
        String roomType,
        @Schema(description = "Valor da diaria.", example = "199.90")
        BigDecimal dailyRate
) {
}
