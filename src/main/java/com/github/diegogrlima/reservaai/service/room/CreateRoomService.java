package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.exception.RoomAlreadyExistsException;
import com.github.diegogrlima.reservaai.mapper.RoomMapper;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomResponseDTO execute(CreateRoomRequestDTO request) {
        if (roomRepository.existsByRoomNumber(request.roomNumber())) {
            throw new RoomAlreadyExistsException(request.roomNumber());
        }

        Room room = roomMapper.toEntity(request);
        Room savedRoom = roomRepository.save(room);

        return roomMapper.toResponse(savedRoom);
    }
}
