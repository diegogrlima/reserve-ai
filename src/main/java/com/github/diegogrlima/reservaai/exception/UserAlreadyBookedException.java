package com.github.diegogrlima.reservaai.exception;

public class UserAlreadyBookedException extends RuntimeException {

    public UserAlreadyBookedException(Long userId) {
        super("User already has a booking: " + userId);
    }
}
