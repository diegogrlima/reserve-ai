package com.github.diegogrlima.reservaai.service.room;

import com.github.diegogrlima.reservaai.domain.enums.BookingStatus;
import com.github.diegogrlima.reservaai.exception.RoomAlreadyBookedException;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
import com.github.diegogrlima.reservaai.repository.BookingRepository;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteRoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public void execute(Long id) {
        log.debug("Iniciando remocao de quarto id={}", id);

        if (!roomRepository.existsById(id)) {
            log.warn("Remocao de quarto bloqueada: quarto nao encontrado id={}", id);
            throw new RoomNotFoundException(id);
        }

        if (bookingRepository.existsByRoomIdAndStatus(id, BookingStatus.CONFIRMED)) {
            log.warn("Remocao de quarto bloqueada por reserva confirmada roomId={}", id);
            throw new RoomAlreadyBookedException(id);
        }

        roomRepository.deleteById(id);
        log.info("Quarto removido com sucesso id={}", id);
    }
}
