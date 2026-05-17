package com.github.diegogrlima.reservaai.repository;

import com.github.diegogrlima.reservaai.domain.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomNumber(String roomNumber);

    boolean existsByRoomNumberAndIdNot(String roomNumber, Long id);
}
