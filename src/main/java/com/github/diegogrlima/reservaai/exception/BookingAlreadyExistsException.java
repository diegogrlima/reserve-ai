package com.github.diegogrlima.reservaai.exception;

public class BookingAlreadyExistsException extends RuntimeException {

    public BookingAlreadyExistsException(Long roomId) {
        super("Booking already registered for room: " + roomId);
    }
}
