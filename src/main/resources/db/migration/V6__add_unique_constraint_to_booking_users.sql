ALTER TABLE bookings
    ADD CONSTRAINT uk_bookings_user UNIQUE (user_id);
