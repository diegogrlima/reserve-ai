package com.github.diegogrlima.reservaai.repository;

import com.github.diegogrlima.reservaai.domain.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
}
