ALTER TABLE rooms
    ADD COLUMN name VARCHAR(50),
    ADD COLUMN description VARCHAR(255),
    ADD COLUMN full_description TEXT,
    ADD COLUMN capacity INT,
    ADD COLUMN image VARCHAR(500),
    ADD COLUMN gallery JSON,
    ADD COLUMN amenities JSON;
