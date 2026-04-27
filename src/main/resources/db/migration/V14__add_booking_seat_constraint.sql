alter table booking
    add constraint uq_booking_flight_seat unique (external_flight_id, seat_id);