package com.github.diegogrlima.reservaai.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long id) {
        super("Room not found: " + id);
    }
}
