package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.request.UpdateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.exception.RoomAlreadyExistsException;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
import com.github.diegogrlima.reservaai.mapper.RoomMapper;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateRoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomResponseDTO execute(Long id, UpdateRoomRequestDTO request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        if (roomRepository.existsByRoomNumberAndIdNot(request.roomNumber(), id)) {
            throw new RoomAlreadyExistsException(request.roomNumber());
        }

        roomMapper.updateEntity(request, room);
        Room updatedRoom = roomRepository.save(room);

        return roomMapper.toResponse(updatedRoom);
    }
}
