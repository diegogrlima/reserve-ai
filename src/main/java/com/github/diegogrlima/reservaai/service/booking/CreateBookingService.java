package com.github.diegogrlima.reservaai.service.booking;

import com.github.diegogrlima.reservaai.domain.model.Booking;
import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.CreateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.exception.BookingAlreadyExistsException;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
import com.github.diegogrlima.reservaai.exception.UserNotFoundException;
import com.github.diegogrlima.reservaai.mapper.BookingMapper;
import com.github.diegogrlima.reservaai.repository.BookingRepository;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateBookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingMapper bookingMapper;

    @Transactional
    public BookingResponseDTO execute(CreateBookingRequestDTO request) {
        if (bookingRepository.existsByRoomId(request.roomId())) {
            throw new BookingAlreadyExistsException(request.roomId());
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RoomNotFoundException(request.roomId()));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);

        Booking savedBooking = bookingRepository.save(booking);

        return bookingMapper.toResponse(savedBooking);
    }
}
