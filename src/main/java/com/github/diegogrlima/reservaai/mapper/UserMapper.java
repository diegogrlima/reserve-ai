package com.github.diegogrlima.reservaai.mapper;

import com.github.diegogrlima.reservaai.dtos.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dtos.response.UserResponseDTO;
import com.github.diegogrlima.reservaai.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(CreateUserRequestDTO request);

    UserResponseDTO toResponse(User user);
}
