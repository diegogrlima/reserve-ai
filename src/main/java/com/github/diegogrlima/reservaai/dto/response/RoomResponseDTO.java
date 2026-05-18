package com.github.diegogrlima.reservaai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Representacao de quarto retornada pela API.")
public record RoomResponseDTO(
        @Schema(description = "Identificador do quarto.", example = "1")
        Long id,

        @Schema(description = "Numero do quarto.", example = "101")
        String roomNumber,

        @Schema(description = "Tipo do quarto.", example = "STANDARD")
        String roomType,

        @Schema(description = "Valor da diaria.", example = "199.90")
        BigDecimal dailyRate,

        @Schema(description = "Nome exibivel do quarto.", example = "Quarto Standard")
        String name,

        @Schema(description = "Descricao curta do quarto.", example = "Quarto aconchegante com cama de casal e vista para o jardim.")
        String description,

        @Schema(description = "Descricao detalhada do quarto.")
        String fullDescription,

        @Schema(description = "Capacidade maxima de hospedes.", example = "2")
        Integer capacity,

        @Schema(description = "URL da imagem principal do quarto.", example = "https://images.unsplash.com/photo-1631049307264")
        String image,

        @Schema(description = "Lista de URLs da galeria de fotos do quarto.")
        List<String> gallery,

        @Schema(description = "Lista de comodidades disponiveis no quarto.")
        List<String> amenities
) {
}
