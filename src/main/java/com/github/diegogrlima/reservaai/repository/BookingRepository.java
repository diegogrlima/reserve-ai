package com.github.diegogrlima.reservaai.repository;

import com.github.diegogrlima.reservaai.domain.enums.BookingStatus;
import com.github.diegogrlima.reservaai.domain.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByRoomIdAndStatus(Long roomId, BookingStatus status);

    boolean existsByRoomIdAndStatusAndIdNot(Long roomId, BookingStatus status, Long id);

    boolean existsByUserIdAndStatus(Long userId, BookingStatus status);

    boolean existsByUserIdAndStatusAndIdNot(Long userId, BookingStatus status, Long id);
}
