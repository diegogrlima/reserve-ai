package com.github.diegogrlima.reservaai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.diegogrlima.reservaai.dto.request.CreateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateRoomRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.RoomResponseDTO;
import com.github.diegogrlima.reservaai.service.room.CreateRoomService;
import com.github.diegogrlima.reservaai.service.room.DeleteRoomService;
import com.github.diegogrlima.reservaai.service.room.GetAllRoomsService;
import com.github.diegogrlima.reservaai.service.room.GetAvailableRoomsService;
import com.github.diegogrlima.reservaai.service.room.GetRoomByIdService;
import com.github.diegogrlima.reservaai.service.room.UpdateRoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoomControllerTest {

    private final CreateRoomService createRoomService = mock(CreateRoomService.class);
    private final GetAllRoomsService getAllRoomsService = mock(GetAllRoomsService.class);
    private final GetAvailableRoomsService getAvailableRoomsService = mock(GetAvailableRoomsService.class);
    private final GetRoomByIdService getRoomByIdService = mock(GetRoomByIdService.class);
    private final UpdateRoomService updateRoomService = mock(UpdateRoomService.class);
    private final DeleteRoomService deleteRoomService = mock(DeleteRoomService.class);

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RoomController roomController;

    @BeforeEach
    void setUp() {
        roomController = new RoomController(
                createRoomService,
                getAllRoomsService,
                getAvailableRoomsService,
                getRoomByIdService,
                updateRoomService,
                deleteRoomService
        );
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(roomController)
                .setValidator(validator)
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createShouldReturnCreatedWhenPayloadIsValid() throws Exception {
        CreateRoomRequestDTO request = createRequest();
        RoomResponseDTO response = response();

        when(createRoomService.execute(any(CreateRoomRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.roomNumber").value("101"));

        verify(createRoomService).execute(any(CreateRoomRequestDTO.class));
    }

    @Test
    void createShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        CreateRoomRequestDTO request = new CreateRoomRequestDTO("", "", BigDecimal.ZERO, null, null, null, null, null, null, null);

        mockMvc.perform(post("/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(createRoomService, never()).execute(any(CreateRoomRequestDTO.class));
    }

    @Test
    void getAllShouldReturnRoomsPage() {
        Page<RoomResponseDTO> page = new PageImpl<>(List.of(response()));

        when(getAllRoomsService.execute(any())).thenReturn(page);

        ResponseEntity<Page<RoomResponseDTO>> response = roomController.getAll(PageRequest.of(0, 20));

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isSameAs(page);
        verify(getAllRoomsService).execute(any());
    }

    @Test
    void getAvailableShouldReturnAvailableRoomsPage() {
        Page<RoomResponseDTO> page = new PageImpl<>(List.of(response()));

        when(getAvailableRoomsService.execute(any())).thenReturn(page);

        ResponseEntity<Page<RoomResponseDTO>> response = roomController.getAvailable(PageRequest.of(0, 20));

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isSameAs(page);
        verify(getAvailableRoomsService).execute(any());
    }

    @Test
    void getByIdShouldReturnRoom() {
        RoomResponseDTO room = response();

        when(getRoomByIdService.execute(10L)).thenReturn(room);

        ResponseEntity<RoomResponseDTO> response = roomController.getById(10L);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isSameAs(room);
        verify(getRoomByIdService).execute(10L);
    }

    @Test
    void updateShouldReturnOkWhenPayloadIsValid() throws Exception {
        UpdateRoomRequestDTO request = updateRequest();
        RoomResponseDTO response = response();

        when(updateRoomService.execute(any(Long.class), any(UpdateRoomRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/rooms/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));

        verify(updateRoomService).execute(any(Long.class), any(UpdateRoomRequestDTO.class));
    }

    @Test
    void updateShouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
        UpdateRoomRequestDTO request = new UpdateRoomRequestDTO("", "", BigDecimal.ZERO, null, null, null, null, null, null, null);

        mockMvc.perform(put("/rooms/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(updateRoomService, never()).execute(any(Long.class), any(UpdateRoomRequestDTO.class));
    }

    @Test
    void deleteShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/rooms/10"))
                .andExpect(status().isNoContent());

        verify(deleteRoomService).execute(10L);
    }

    private CreateRoomRequestDTO createRequest() {
        return new CreateRoomRequestDTO("101", "STANDARD", new BigDecimal("200.00"), null, null, null, 2, null, List.of(), List.of());
    }

    private UpdateRoomRequestDTO updateRequest() {
        return new UpdateRoomRequestDTO("101", "STANDARD", new BigDecimal("200.00"), null, null, null, 2, null, List.of(), List.of());
    }

    private RoomResponseDTO response() {
        return new RoomResponseDTO(10L, "101", "STANDARD", new BigDecimal("200.00"), null, null, null, 2, null, List.of(), List.of());
    }
}
