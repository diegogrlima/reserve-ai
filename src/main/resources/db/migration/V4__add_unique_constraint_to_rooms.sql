ALTER TABLE rooms
    ADD CONSTRAINT uk_rooms_room_number UNIQUE (room_number);
