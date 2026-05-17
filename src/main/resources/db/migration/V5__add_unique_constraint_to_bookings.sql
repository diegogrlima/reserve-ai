ALTER TABLE bookings
    ADD CONSTRAINT uk_bookings_room UNIQUE (room_id);
