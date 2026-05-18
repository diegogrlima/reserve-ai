package com.github.diegogrlima.reservaai.exception;

public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(Long id) {
        super("Booking not found: " + id);
    }
}
