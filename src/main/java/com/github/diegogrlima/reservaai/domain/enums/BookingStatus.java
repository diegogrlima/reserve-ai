package com.github.diegogrlima.reservaai.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status da reserva.")
public enum BookingStatus {
    CONFIRMED,
    CANCELED
}
