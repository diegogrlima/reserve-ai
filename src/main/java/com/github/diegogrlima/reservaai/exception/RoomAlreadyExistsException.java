package com.github.diegogrlima.reservaai.exception;

public class RoomAlreadyExistsException extends RuntimeException {

    public RoomAlreadyExistsException(String roomNumber) {
        super("Room already registered: " + roomNumber);
    }
}
