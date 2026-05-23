package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.exception.RoomAlreadyExistsException;
import com.github.diegogrlima.reservaai.mapper.JsonConverter;
import com.github.diegogrlima.reservaai.mapper.RoomMapper;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateRoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final JsonConverter jsonConverter;

    @Transactional
    public RoomResponseDTO execute(CreateRoomRequestDTO request) {
        log.debug("Iniciando cadastro de quarto roomNumber={}", request.roomNumber());

        if (roomRepository.existsByRoomNumber(request.roomNumber())) {
            log.warn("Cadastro de quarto bloqueado por numero ja existente roomNumber={}", request.roomNumber());
            throw new RoomAlreadyExistsException(request.roomNumber());
        }

        Room room = roomMapper.toEntity(request);

        if (request.gallery() != null) {
            room.setGallery(jsonConverter.listToJson(request.gallery()));
        }
        if (request.amenities() != null) {
            room.setAmenities(jsonConverter.listToJson(request.amenities()));
        }

        Room savedRoom = roomRepository.save(room);

        log.info("Quarto cadastrado com sucesso id={} roomNumber={}",
                savedRoom.getId(), savedRoom.getRoomNumber());

        return roomMapper.toResponse(savedRoom);
    }
}
