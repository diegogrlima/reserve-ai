package com.github.diegogrlima.reservaai.controller;

import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.ErrorResponseDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.service.room.CreateRoomService;
import com.github.diegogrlima.reservaai.service.room.DeleteRoomService;
import com.github.diegogrlima.reservaai.service.room.GetAllRoomsService;
import com.github.diegogrlima.reservaai.service.room.GetRoomByIdService;
import com.github.diegogrlima.reservaai.service.room.UpdateRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(name = "Quartos", description = "Operações de cadastro, consulta, atualização e remoção de quartos.")
public class RoomController {

    private final CreateRoomService createRoomService;
    private final GetAllRoomsService getAllRoomsService;
    private final GetRoomByIdService getRoomByIdService;
    private final UpdateRoomService updateRoomService;
    private final DeleteRoomService deleteRoomService;

    @PostMapping
    @Operation(summary = "Cadastrar quarto", description = "Cria um novo quarto com número único.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Quarto cadastrado com sucesso",
                    content = @Content(schema = @Schema(implementation = RoomResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Número do quarto já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<RoomResponseDTO> create(@Valid @RequestBody CreateRoomRequestDTO request) {
        RoomResponseDTO response = createRoomService.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar quartos", description = "Retorna a listagem paginada de quartos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de quartos retornada com sucesso")
    })
    public ResponseEntity<Page<RoomResponseDTO>> getAll(@ParameterObject Pageable pageable) {
        Page<RoomResponseDTO> response = getAllRoomsService.execute(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar quarto por ID", description = "Retorna um quarto pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quarto encontrado",
                    content = @Content(schema = @Schema(implementation = RoomResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Quarto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<RoomResponseDTO> getById(@PathVariable Long id) {
        RoomResponseDTO response = getRoomByIdService.execute(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar quarto", description = "Atualiza os dados de um quarto sem permitir duplicidade do número.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quarto atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = RoomResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Quarto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Número do quarto já cadastrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<RoomResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoomRequestDTO request
    ) {
        RoomResponseDTO response = updateRoomService.execute(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover quarto", description = "Remove um quarto por ID. Bloqueia a exclusão se houver reserva vinculada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Quarto removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quarto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Quarto possui reserva vinculada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteRoomService.execute(id);

        return ResponseEntity.noContent().build();
    }
}
