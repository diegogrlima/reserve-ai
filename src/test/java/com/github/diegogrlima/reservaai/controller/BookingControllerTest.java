package com.github.diegogrlima.reservaai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.diegogrlima.reservaai.dto.request.CreateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.request.UpdateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.service.booking.CancelBookingService;
import com.github.diegogrlima.reservaai.service.booking.CreateBookingService;
import com.github.diegogrlima.reservaai.service.booking.GetAllBookingsService;
import com.github.diegogrlima.reservaai.service.booking.UpdateBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookingControllerTest {

    private final CreateBookingService createBookingService = mock(CreateBookingService.class);
    private final GetAllBookingsService getAllBookingsService = mock(GetAllBookingsService.class);
    private final UpdateBookingService updateBookingService = mock(UpdateBookingService.class);
    private final CancelBookingService cancelBookingService = mock(CancelBookingService.class);

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        bookingController = new BookingController(
                createBookingService,
                getAllBookingsService,
                updateBookingService,
                cancelBookingService
        );
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setValidator(validator)
                .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void createShouldReturnCreatedWhenPayloadIsValid() throws Exception {
        CreateBookingRequestDTO request = validCreateRequest();
        BookingResponseDTO response = response();

        when(createBookingService.execute(any(CreateBookingRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.userId").value(response.userId()))
                .andExpect(jsonPath("$.roomId").value(response.roomId()))
                .andExpect(jsonPath("$.status").value(response.status()));

        verify(createBookingService).execute(any(CreateBookingRequestDTO.class));
    }

    @Test
    void createShouldReturnBadRequestWhenCheckOutIsBeforeCheckIn() throws Exception {
        CreateBookingRequestDTO request = new CreateBookingRequestDTO(
                1L,
                10L,
                LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(2)
        );

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(createBookingService, never()).execute(any(CreateBookingRequestDTO.class));
    }

    @Test
    void createShouldReturnBadRequestWhenRequiredFieldsAreMissing() throws Exception {
        String payload = "{}";

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());

        verify(createBookingService, never()).execute(any(CreateBookingRequestDTO.class));
    }

    @Test
    void getAllShouldReturnBookingsPage() throws Exception {
        Page<BookingResponseDTO> page = new PageImpl<>(List.of(response()));

        when(getAllBookingsService.execute(any())).thenReturn(page);

        ResponseEntity<Page<BookingResponseDTO>> response = bookingController.getAll(PageRequest.of(0, 20));

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isSameAs(page);
        verify(getAllBookingsService).execute(any());
    }

    @Test
    void updateShouldReturnOkWhenPayloadIsValid() throws Exception {
        UpdateBookingRequestDTO request = new UpdateBookingRequestDTO(1L, 10L);
        BookingResponseDTO response = response();

        when(updateBookingService.execute(any(Long.class), any(UpdateBookingRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/bookings/30")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(updateBookingService).execute(any(Long.class), any(UpdateBookingRequestDTO.class));
    }

    @Test
    void cancelShouldReturnOk() throws Exception {
        BookingResponseDTO response = response();

        when(cancelBookingService.execute(30L)).thenReturn(response);

        mockMvc.perform(patch("/bookings/30/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(cancelBookingService).execute(30L);
    }

    private CreateBookingRequestDTO validCreateRequest() {
        return new CreateBookingRequestDTO(
                1L,
                10L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(4)
        );
    }

    private BookingResponseDTO response() {
        return new BookingResponseDTO(
                30L,
                1L,
                "Diego Lima",
                10L,
                "101",
                "CONFIRMED",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(4),
                new BigDecimal("600.00")
        );
    }
}
