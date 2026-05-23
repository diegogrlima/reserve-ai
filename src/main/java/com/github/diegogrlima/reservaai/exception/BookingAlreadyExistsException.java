package com.github.diegogrlima.reservaai.exception;

public class BookingAlreadyExistsException extends RuntimeException {

    public BookingAlreadyExistsException(Long roomId) {
        super("Room already has a confirmed booking in the selected period: " + roomId);
    }
}
