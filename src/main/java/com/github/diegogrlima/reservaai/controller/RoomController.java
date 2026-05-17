package com.github.diegogrlima.reservaai.controller;

import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.service.room.CreateRoomService;
import com.github.diegogrlima.reservaai.service.room.GetAllRoomsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final CreateRoomService createRoomService;
    private final GetAllRoomsService getAllRoomsService;

    @PostMapping
    public ResponseEntity<RoomResponseDTO> create(@Valid @RequestBody CreateRoomRequestDTO request) {
        RoomResponseDTO response = createRoomService.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<RoomResponseDTO>> getAll(Pageable pageable) {
        Page<RoomResponseDTO> response = getAllRoomsService.execute(pageable);

        return ResponseEntity.ok(response);
    }
}
