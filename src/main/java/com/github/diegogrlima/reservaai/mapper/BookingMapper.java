package com.github.diegogrlima.reservaai.mapper;

import com.github.diegogrlima.reservaai.domain.model.Booking;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.name")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "roomNumber", source = "room.roomNumber")
    BookingResponseDTO toResponse(Booking booking);
}
