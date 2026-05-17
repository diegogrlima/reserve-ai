package com.github.diegogrlima.reservaai.controller;

import com.github.diegogrlima.reservaai.dto.request.CreateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.service.booking.CreateBookingService;
import com.github.diegogrlima.reservaai.service.booking.GetAllBookingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final CreateBookingService createBookingService;
    private  final GetAllBookingsService getAllBookingsService;

    @PostMapping
    public ResponseEntity<BookingResponseDTO> create(@Valid @RequestBody CreateBookingRequestDTO request) {
        BookingResponseDTO response = createBookingService.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<BookingResponseDTO>> getAll(Pageable pageable) {
        return  ResponseEntity.ok(getAllBookingsService.execute(pageable));
    }
}
