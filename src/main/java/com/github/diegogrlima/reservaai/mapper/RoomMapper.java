package com.github.diegogrlima.reservaai.mapper;

import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {

    @Autowired
    protected JsonConverter jsonConverter;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    public abstract Room toEntity(CreateRoomRequestDTO request);

    @AfterMapping
    protected void afterCreateToEntity(CreateRoomRequestDTO request, @MappingTarget Room room) {
        if (request.gallery() != null) {
            room.setGallery(jsonConverter.listToJson(request.gallery()));
        }
        if (request.amenities() != null) {
            room.setAmenities(jsonConverter.listToJson(request.amenities()));
        }
    }

    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    public abstract RoomResponseDTO toResponseBase(Room room);

    public RoomResponseDTO toResponse(Room room) {
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
                jsonConverter.jsonToList(room.getGallery()),
                jsonConverter.jsonToList(room.getAmenities())
        );
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    public abstract void updateEntity(UpdateRoomRequestDTO request, @MappingTarget Room room);
}
