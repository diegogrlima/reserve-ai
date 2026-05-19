package com.github.diegogrlima.reservaai.mapper;

import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.CreateUserRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(CreateUserRequestDTO request);

    UserResponseDTO toResponse(User user);
}
