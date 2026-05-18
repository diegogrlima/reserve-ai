package com.github.diegogrlima.reservaai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Dados para atualizacao de quarto.")
public record UpdateRoomRequestDTO(
        @Schema(description = "Numero unico do quarto.", example = "102")
        @NotBlank
        @Size(max = 15)
        String roomNumber,

        @Schema(description = "Tipo do quarto.", example = "DELUXE")
        @NotBlank
        @Size(max = 20)
        String roomType,

        @Schema(description = "Valor da diaria.", example = "250.00")
        @NotNull
        @Positive
        BigDecimal dailyRate,

        @Schema(description = "Nome exibivel do quarto.", example = "Quarto Superior")
        String name,

        @Schema(description = "Descricao curta do quarto.")
        String description,

        @Schema(description = "Descricao detalhada do quarto.")
        String fullDescription,

        @Schema(description = "Capacidade maxima de hospedes.", example = "3")
        Integer capacity,

        @Schema(description = "URL da imagem principal do quarto.")
        String image,

        @Schema(description = "Lista de URLs da galeria de fotos.")
        List<String> gallery,

        @Schema(description = "Lista de comodidades disponiveis.")
        List<String> amenities
) {
}
