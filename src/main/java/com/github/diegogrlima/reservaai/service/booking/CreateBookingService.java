package com.github.diegogrlima.reservaai.service.booking;

import com.github.diegogrlima.reservaai.domain.enums.BookingStatus;
import com.github.diegogrlima.reservaai.domain.model.Booking;
import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.CreateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.exception.BookingAlreadyExistsException;
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
public class CreateBookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponseDTO execute(CreateBookingRequestDTO request) {
        log.debug("Iniciando cadastro de reserva userId={} roomId={} checkIn={} checkOut={}",
                request.userId(), request.roomId(), request.checkIn(), request.checkOut());

        if (bookingRepository.existsByUserIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.userId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )) {
            log.warn("Cadastro de reserva bloqueado por conflito de usuario userId={} checkIn={} checkOut={}",
                    request.userId(), request.checkIn(), request.checkOut());
            throw new UserAlreadyBookedException(request.userId());
        }

        if (bookingRepository.existsByRoomIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.roomId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )) {
            log.warn("Cadastro de reserva bloqueado por conflito de quarto roomId={} checkIn={} checkOut={}",
                    request.roomId(), request.checkIn(), request.checkOut());
            throw new BookingAlreadyExistsException(request.roomId());
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> {
                    log.warn("Cadastro de reserva bloqueado: usuario nao encontrado userId={}", request.userId());
                    return new UserNotFoundException(request.userId());
                });
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> {
                    log.warn("Cadastro de reserva bloqueado: quarto nao encontrado roomId={}", request.roomId());
                    return new RoomNotFoundException(request.roomId());
                });

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setCheckIn(request.checkIn());
        booking.setCheckOut(request.checkOut());

        Booking savedBooking = bookingRepository.save(booking);

        log.info("Reserva cadastrada com sucesso id={} userId={} roomId={} status={}",
                savedBooking.getId(), user.getId(), room.getId(), savedBooking.getStatus());

        return bookingMapper.toResponse(savedBooking);
    }
}
