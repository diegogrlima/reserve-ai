package com.github.diegogrlima.reservaai.mapper;

import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Room toEntity(CreateRoomRequestDTO request);

    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    RoomResponseDTO toResponseBase(Room room);

    default RoomResponseDTO toResponse(Room room) {
        RoomResponseDTO base = toResponseBase(room);
        return new RoomResponseDTO(
                base.id(),
                base.roomNumber(),
                base.roomType(),
                base.dailyRate(),
                base.name(),
                base.description(),
                base.fullDescription(),
                base.capacity(),
                base.image(),
                parseJsonList(room.getGallery()),
                parseJsonList(room.getAmenities())
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    void updateEntity(UpdateRoomRequestDTO request, @MappingTarget Room room);

    default List<String> parseJsonList(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
