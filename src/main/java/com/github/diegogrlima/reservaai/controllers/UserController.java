package com.github.diegogrlima.reservaai.controllers;

import com.github.diegogrlima.reservaai.dtos.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dtos.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.services.users.CreateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserService createUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@Valid @RequestBody CreateUserRequestDTO request) {
        return createUserService.execute(request);
    }
}
