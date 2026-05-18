package com.github.diegogrlima.reservaai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Dados para cadastro de quarto.")
public record CreateRoomRequestDTO(
        @Schema(description = "Numero unico do quarto.", example = "101")
        @NotBlank
        @Size(max = 15)
        String roomNumber,

        @Schema(description = "Tipo do quarto.", example = "STANDARD")
        @NotBlank
        @Size(max = 20)
        String roomType,

        @Schema(description = "Valor da diaria.", example = "199.90")
        @NotNull
        @Positive
        BigDecimal dailyRate,

        @Schema(description = "Nome exibivel do quarto.", example = "Quarto Standard")
        String name,

        @Schema(description = "Descricao curta do quarto.", example = "Quarto aconchegante com cama de casal.")
        String description,

        @Schema(description = "Descricao detalhada do quarto.")
        String fullDescription,

        @Schema(description = "Capacidade maxima de hospedes.", example = "2")
        Integer capacity,

        @Schema(description = "URL da imagem principal do quarto.", example = "https://images.unsplash.com/photo-1631049307264")
        String image,

        @Schema(description = "Lista de URLs da galeria de fotos.")
        List<String> gallery,

        @Schema(description = "Lista de comodidades disponiveis.")
        List<String> amenities
) {
}
