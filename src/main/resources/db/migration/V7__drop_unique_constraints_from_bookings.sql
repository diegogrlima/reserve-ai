ALTER TABLE bookings
    ADD INDEX idx_bookings_room_id (room_id),
    ADD INDEX idx_bookings_user_id (user_id);

ALTER TABLE bookings
    DROP INDEX uk_bookings_room;

ALTER TABLE bookings
    DROP INDEX uk_bookings_user;
