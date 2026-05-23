package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.dto.request.UpdateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.exception.RoomAlreadyExistsException;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
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
public class UpdateRoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final JsonConverter jsonConverter;

    @Transactional
    public RoomResponseDTO execute(Long id, UpdateRoomRequestDTO request) {
        log.debug("Iniciando atualizacao de quarto id={} roomNumber={}", id, request.roomNumber());

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Atualizacao de quarto bloqueada: quarto nao encontrado id={}", id);
                    return new RoomNotFoundException(id);
                });

        if (roomRepository.existsByRoomNumberAndIdNot(request.roomNumber(), id)) {
            log.warn("Atualizacao de quarto bloqueada por numero ja existente id={} roomNumber={}",
                    id, request.roomNumber());
            throw new RoomAlreadyExistsException(request.roomNumber());
        }

        roomMapper.updateEntity(request, room);

        if (request.gallery() != null) {
            room.setGallery(jsonConverter.listToJson(request.gallery()));
        }
        if (request.amenities() != null) {
            room.setAmenities(jsonConverter.listToJson(request.amenities()));
        }

        Room updatedRoom = roomRepository.save(room);

        log.info("Quarto atualizado com sucesso id={} roomNumber={}",
                updatedRoom.getId(), updatedRoom.getRoomNumber());

        return roomMapper.toResponse(updatedRoom);
    }
}
