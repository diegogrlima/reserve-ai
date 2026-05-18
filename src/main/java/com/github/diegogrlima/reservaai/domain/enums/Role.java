package com.github.diegogrlima.reservaai.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Perfil de acesso do usuário.")
public enum Role {
    USER,
    ADMIN
}
