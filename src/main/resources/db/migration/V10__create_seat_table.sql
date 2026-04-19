CREATE TABLE seat (
                      id BIGSERIAL PRIMARY KEY,
                      seat_number BIGINT,
                      is_window BOOLEAN,
                      is_aisle BOOLEAN,
                      is_extra_legroom BOOLEAN
);

ALTER TABLE booking
    ADD COLUMN seat_id BIGINT;

ALTER TABLE booking
    ADD CONSTRAINT fk_booking_seat
        FOREIGN KEY (seat_id) REFERENCES seat(id);