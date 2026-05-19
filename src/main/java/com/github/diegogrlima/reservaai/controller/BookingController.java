package com.github.diegogrlima.reservaai.controller;

import com.github.diegogrlima.reservaai.dto.request.CreateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.dto.response.ErrorResponseDTO;
import com.github.diegogrlima.reservaai.service.booking.CancelBookingService;
import com.github.diegogrlima.reservaai.service.booking.CreateBookingService;
import com.github.diegogrlima.reservaai.service.booking.GetAllBookingsService;
import com.github.diegogrlima.reservaai.service.booking.UpdateBookingService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Operações de cadastro, consulta, atualização e cancelamento de reservas.")
public class BookingController {

    private final CreateBookingService createBookingService;
    private final GetAllBookingsService getAllBookingsService;
    private final UpdateBookingService updateBookingService;
    private final CancelBookingService cancelBookingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Cadastrar reserva", description = "Cria uma reserva vinculada a um usuário e a um quarto com status CONFIRMED.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva cadastrada com sucesso",
                    content = @Content(schema = @Schema(implementation = BookingResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário ou quarto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Reserva duplicada para usuário ou quarto",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<BookingResponseDTO> create(@Valid @RequestBody CreateBookingRequestDTO request) {
        BookingResponseDTO response = createBookingService.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar reservas", description = "Retorna a listagem paginada de reservas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reservas retornada com sucesso")
    })
    public ResponseEntity<Page<BookingResponseDTO>> getAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(getAllBookingsService.execute(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar reserva", description = "Atualiza usuário e quarto de uma reserva já existente. Só bloqueia conflitos com reservas CONFIRMED.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva atualizada com sucesso",
                    content = @Content(schema = @Schema(implementation = BookingResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva, usuário ou quarto não encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Reserva duplicada para usuário ou quarto",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<BookingResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingRequestDTO request
    ) {
        BookingResponseDTO response = updateBookingService.execute(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Cancelar reserva", description = "Altera o status da reserva para CANCELED.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada com sucesso",
                    content = @Content(schema = @Schema(implementation = BookingResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<BookingResponseDTO> cancel(@PathVariable Long id) {
        BookingResponseDTO response = cancelBookingService.execute(id);

        return ResponseEntity.ok(response);
    }
}
