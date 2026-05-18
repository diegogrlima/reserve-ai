package com.github.diegogrlima.reservaai.controller;

import com.github.diegogrlima.reservaai.dto.request.CreateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.service.booking.CreateBookingService;
import com.github.diegogrlima.reservaai.service.booking.CancelBookingService;
import com.github.diegogrlima.reservaai.service.booking.GetAllBookingsService;
import com.github.diegogrlima.reservaai.service.booking.UpdateBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class BookingController {

    private final CreateBookingService createBookingService;
    private final GetAllBookingsService getAllBookingsService;
    private final UpdateBookingService updateBookingService;
    private final CancelBookingService cancelBookingService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(@Valid @RequestBody CreateBookingRequestDTO request) {
        BookingResponseDTO response = createBookingService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<BookingResponseDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(getAllBookingsService.execute(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookingRequestDTO request
    ) {
        BookingResponseDTO response = updateBookingService.execute(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDTO> cancel(@PathVariable Long id) {
        BookingResponseDTO response = cancelBookingService.execute(id);

        return ResponseEntity.ok(response);
    }
}
