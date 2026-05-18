package com.github.diegogrlima.reservaai.repository;

import com.github.diegogrlima.reservaai.domain.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByRoomId(Long roomId);

    boolean existsByRoomIdAndIdNot(Long roomId, Long id);

    boolean existsByUserId(Long userId);

    boolean existsByUserIdAndIdNot(Long userId, Long id);
}
