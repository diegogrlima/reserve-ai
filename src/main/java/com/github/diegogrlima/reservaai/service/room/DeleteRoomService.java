package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.domain.enums.BookingStatus;
import com.github.diegogrlima.reservaai.exception.RoomAlreadyBookedException;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
import com.github.diegogrlima.reservaai.repository.BookingRepository;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteRoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public void execute(Long id) {
        if (!roomRepository.existsById(id)) {
            throw new RoomNotFoundException(id);
        }

        if (bookingRepository.existsByRoomIdAndStatus(id, BookingStatus.CONFIRMED)) {
            throw new RoomAlreadyBookedException(id);
        }

        roomRepository.deleteById(id);
    }
}
