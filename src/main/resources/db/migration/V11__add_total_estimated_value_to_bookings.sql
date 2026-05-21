ALTER TABLE bookings
    ADD COLUMN total_estimated_value DECIMAL(10, 2) NULL;

UPDATE bookings b
INNER JOIN rooms r ON r.id = b.room_id
SET b.total_estimated_value = r.daily_rate * DATEDIFF(b.check_out, b.check_in)
WHERE b.total_estimated_value IS NULL;

ALTER TABLE bookings
    MODIFY COLUMN total_estimated_value DECIMAL(10, 2) NOT NULL;
