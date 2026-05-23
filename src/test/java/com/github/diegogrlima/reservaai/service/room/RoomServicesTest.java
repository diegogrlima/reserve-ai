package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.domain.enums.BookingStatus;
import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.exception.RoomAlreadyBookedException;
import com.github.diegogrlima.reservaai.exception.RoomAlreadyExistsException;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
import com.github.diegogrlima.reservaai.mapper.JsonConverter;
import com.github.diegogrlima.reservaai.mapper.RoomMapper;
import com.github.diegogrlima.reservaai.repository.BookingRepository;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServicesTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private JsonConverter jsonConverter;

    @InjectMocks
    private CreateRoomService createRoomService;

    @InjectMocks
    private GetAllRoomsService getAllRoomsService;

    @InjectMocks
    private GetAvailableRoomsService getAvailableRoomsService;

    @InjectMocks
    private GetRoomByIdService getRoomByIdService;

    @InjectMocks
    private UpdateRoomService updateRoomService;

    @InjectMocks
    private DeleteRoomService deleteRoomService;

    @Test
    void createShouldBlockDuplicateRoomNumber() {
        CreateRoomRequestDTO request = createRequest();

        when(roomRepository.existsByRoomNumber(request.roomNumber())).thenReturn(true);

        assertThatThrownBy(() -> createRoomService.execute(request))
                .isInstanceOf(RoomAlreadyExistsException.class);

        verify(roomRepository, never()).save(any());
    }

    @Test
    void createShouldSaveRoom() {
        CreateRoomRequestDTO request = createRequest();
        Room room = room();
        RoomResponseDTO response = response();

        when(roomRepository.existsByRoomNumber(request.roomNumber())).thenReturn(false);
        when(roomMapper.toEntity(request)).thenReturn(room);
        when(jsonConverter.listToJson(request.gallery())).thenReturn("[]");
        when(jsonConverter.listToJson(request.amenities())).thenReturn("[]");
        when(roomRepository.save(room)).thenReturn(room);
        when(roomMapper.toResponse(room)).thenReturn(response);

        RoomResponseDTO result = createRoomService.execute(request);

        assertThat(result).isSameAs(response);
        verify(roomRepository).save(room);
    }

    @Test
    void getAllShouldMapRooms() {
        Room room = room();
        RoomResponseDTO response = response();

        when(roomRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(room)));
        when(roomMapper.toResponse(room)).thenReturn(response);

        Page<RoomResponseDTO> result = getAllRoomsService.execute(PageRequest.of(0, 20));

        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void getAvailableShouldMapAvailableRooms() {
        Room room = room();
        RoomResponseDTO response = response();

        when(roomRepository.findAvailableRooms(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(room)));
        when(roomMapper.toResponse(room)).thenReturn(response);

        Page<RoomResponseDTO> result = getAvailableRoomsService.execute(PageRequest.of(0, 20));

        assertThat(result.getContent()).containsExactly(response);
    }

    @Test
    void getByIdShouldThrowWhenRoomDoesNotExist() {
        when(roomRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getRoomByIdService.execute(10L))
                .isInstanceOf(RoomNotFoundException.class);
    }

    @Test
    void updateShouldBlockDuplicateRoomNumber() {
        UpdateRoomRequestDTO request = updateRequest();
        Room room = room();

        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));
        when(roomRepository.existsByRoomNumberAndIdNot(request.roomNumber(), 10L)).thenReturn(true);

        assertThatThrownBy(() -> updateRoomService.execute(10L, request))
                .isInstanceOf(RoomAlreadyExistsException.class);

        verify(roomRepository, never()).save(any());
    }

    @Test
    void updateShouldSaveRoom() {
        UpdateRoomRequestDTO request = updateRequest();
        Room room = room();
        RoomResponseDTO response = response();

        when(roomRepository.findById(10L)).thenReturn(Optional.of(room));
        when(roomRepository.existsByRoomNumberAndIdNot(request.roomNumber(), 10L)).thenReturn(false);
        when(jsonConverter.listToJson(request.gallery())).thenReturn("[]");
        when(jsonConverter.listToJson(request.amenities())).thenReturn("[]");
        when(roomRepository.save(room)).thenReturn(room);
        when(roomMapper.toResponse(room)).thenReturn(response);

        RoomResponseDTO result = updateRoomService.execute(10L, request);

        assertThat(result).isSameAs(response);
        verify(roomMapper).updateEntity(request, room);
        verify(roomRepository).save(room);
    }

    @Test
    void deleteShouldThrowWhenRoomDoesNotExist() {
        when(roomRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> deleteRoomService.execute(10L))
                .isInstanceOf(RoomNotFoundException.class);

        verify(roomRepository, never()).deleteById(10L);
    }

    @Test
    void deleteShouldThrowWhenRoomHasConfirmedBooking() {
        when(roomRepository.existsById(10L)).thenReturn(true);
        when(bookingRepository.existsByRoomIdAndStatus(10L, BookingStatus.CONFIRMED)).thenReturn(true);

        assertThatThrownBy(() -> deleteRoomService.execute(10L))
                .isInstanceOf(RoomAlreadyBookedException.class);

        verify(roomRepository, never()).deleteById(10L);
    }

    @Test
    void deleteShouldRemoveRoom() {
        when(roomRepository.existsById(10L)).thenReturn(true);
        when(bookingRepository.existsByRoomIdAndStatus(10L, BookingStatus.CONFIRMED)).thenReturn(false);

        deleteRoomService.execute(10L);

        verify(roomRepository).deleteById(10L);
    }

    private CreateRoomRequestDTO createRequest() {
        return new CreateRoomRequestDTO("101", "STANDARD", new BigDecimal("200.00"), null, null, null, 2, null, List.of(), List.of());
    }

    private UpdateRoomRequestDTO updateRequest() {
        return new UpdateRoomRequestDTO("101", "STANDARD", new BigDecimal("200.00"), null, null, null, 2, null, List.of(), List.of());
    }

    private Room room() {
        Room room = new Room();
        room.setId(10L);
        room.setRoomNumber("101");
        room.setRoomType("STANDARD");
        room.setDailyRate(new BigDecimal("200.00"));
        return room;
    }

    private RoomResponseDTO response() {
        return new RoomResponseDTO(10L, "101", "STANDARD", new BigDecimal("200.00"), null, null, null, 2, null, List.of(), List.of());
    }
}
