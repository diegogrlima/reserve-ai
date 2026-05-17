package com.github.diegogrlima.reservaai.exception;

public class RoomAlreadyBookedException extends RuntimeException {

    public RoomAlreadyBookedException(Long roomId) {
        super("Room already has a booking: " + roomId);
    }
}
