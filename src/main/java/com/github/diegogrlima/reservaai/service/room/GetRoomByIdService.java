package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
import com.github.diegogrlima.reservaai.mapper.RoomMapper;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetRoomByIdService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public RoomResponseDTO execute(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RoomNotFoundException(id));

        return roomMapper.toResponse(room);
    }
}
