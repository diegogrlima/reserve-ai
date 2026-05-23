package com.github.diegogrlima.reservaai.service.booking;

import com.github.diegogrlima.reservaai.domain.enums.BookingStatus;
import com.github.diegogrlima.reservaai.domain.model.Booking;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.exception.BookingNotFoundException;
import com.github.diegogrlima.reservaai.mapper.BookingMapper;
import com.github.diegogrlima.reservaai.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CancelBookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponseDTO execute(Long id) {
        log.debug("Iniciando cancelamento de reserva id={}", id);

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cancelamento de reserva bloqueado: reserva nao encontrada id={}", id);
                    return new BookingNotFoundException(id);
                });

        booking.setStatus(BookingStatus.CANCELED);

        Booking canceledBooking = bookingRepository.save(booking);

        log.info("Reserva cancelada com sucesso id={} status={}",
                canceledBooking.getId(), canceledBooking.getStatus());

        return bookingMapper.toResponse(canceledBooking);
    }
}
