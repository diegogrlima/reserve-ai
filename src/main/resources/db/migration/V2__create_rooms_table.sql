CREATE TABLE rooms (
    id BIGINT NOT NULL AUTO_INCREMENT,
    room_number VARCHAR(15) NOT NULL,
    room_type VARCHAR(20) NOT NULL,
    daily_rate DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id)
);
