package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.mapper.RoomMapper;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllRoomsService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    @Transactional(readOnly = true)
    public Page<RoomResponseDTO> execute(Pageable pageable) {
        log.debug("Listando quartos page={} size={} sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        return roomRepository.findAll(pageable)
                .map(roomMapper::toResponse);
    }
}
