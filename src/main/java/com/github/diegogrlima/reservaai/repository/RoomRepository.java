package com.github.diegogrlima.reservaai.repository;

import com.github.diegogrlima.reservaai.domain.model.Booking;
import com.github.diegogrlima.reservaai.domain.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    boolean existsByRoomNumber(String roomNumber);

    boolean existsByRoomNumberAndIdNot(String roomNumber, Long id);

    @Query("""
            select r
            from Room r
            where r.id not in (
                select b.room.id
                from Booking b
                where b.status = com.github.diegogrlima.reservaai.domain.enums.BookingStatus.CONFIRMED
            )
            """)
    Page<Room> findAvailableRooms(Pageable pageable);
}
