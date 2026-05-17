package com.github.diegogrlima.reservaai.mapper;

import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(target = "id", ignore = true)
    Room toEntity(CreateRoomRequestDTO request);

    RoomResponseDTO toResponse(Room room);

    @Mapping(target = "id", ignore = true)
    void updateEntity(UpdateRoomRequestDTO request, @MappingTarget Room room);
}
