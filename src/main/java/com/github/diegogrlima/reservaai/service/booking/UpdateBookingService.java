package com.github.diegogrlima.reservaai.service.booking;

import com.github.diegogrlima.reservaai.domain.enums.BookingStatus;
import com.github.diegogrlima.reservaai.domain.model.Booking;
import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.UpdateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.exception.BookingAlreadyExistsException;
import com.github.diegogrlima.reservaai.exception.BookingNotFoundException;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
import com.github.diegogrlima.reservaai.exception.UserAlreadyBookedException;
import com.github.diegogrlima.reservaai.exception.UserNotFoundException;
import com.github.diegogrlima.reservaai.mapper.BookingMapper;
import com.github.diegogrlima.reservaai.repository.BookingRepository;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateBookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponseDTO execute(Long id, UpdateBookingRequestDTO request) {
        log.debug("Iniciando atualizacao de reserva id={} userId={} roomId={}",
                id, request.userId(), request.roomId());

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Atualizacao de reserva bloqueada: reserva nao encontrada id={}", id);
                    return new BookingNotFoundException(id);
                });

        if (bookingRepository.existsByRoomIdAndStatusAndIdNot(request.roomId(), BookingStatus.CONFIRMED, id)) {
            log.warn("Atualizacao de reserva bloqueada por conflito de quarto id={} roomId={}", id, request.roomId());
            throw new BookingAlreadyExistsException(request.roomId());
        }

        if (bookingRepository.existsByUserIdAndStatusAndIdNot(request.userId(), BookingStatus.CONFIRMED, id)) {
            log.warn("Atualizacao de reserva bloqueada por conflito de usuario id={} userId={}", id, request.userId());
            throw new UserAlreadyBookedException(request.userId());
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.warn("Atualizacao de reserva bloqueada: usuario nao encontrado userId={}", request.userId());
                    return new UserNotFoundException(request.userId());
                });
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> {
                    log.warn("Atualizacao de reserva bloqueada: quarto nao encontrado roomId={}", request.roomId());
                    return new RoomNotFoundException(request.roomId());
                });

        booking.setUser(user);
        booking.setRoom(room);

        Booking updatedBooking = bookingRepository.save(booking);

        log.info("Reserva atualizada com sucesso id={} userId={} roomId={} status={}",
                updatedBooking.getId(), user.getId(), room.getId(), updatedBooking.getStatus());

        return bookingMapper.toResponse(updatedBooking);
    }
}
