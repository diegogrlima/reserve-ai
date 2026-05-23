package com.github.diegogrlima.reservaai.service.booking;

import com.github.diegogrlima.reservaai.domain.enums.BookingStatus;
import com.github.diegogrlima.reservaai.domain.model.Booking;
import com.github.diegogrlima.reservaai.domain.model.Room;
import com.github.diegogrlima.reservaai.domain.model.User;
import com.github.diegogrlima.reservaai.dto.request.CreateBookingRequestDTO;
import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.exception.BookingAlreadyExistsException;
import com.github.diegogrlima.reservaai.exception.RoomNotFoundException;
import com.github.diegogrlima.reservaai.exception.UserAlreadyBookedException;
import com.github.diegogrlima.reservaai.exception.UserNotFoundException;
import com.github.diegogrlima.reservaai.mapper.BookingMapper;
import com.github.diegogrlima.reservaai.repository.BookingRepository;
import com.github.diegogrlima.reservaai.repository.RoomRepository;
import com.github.diegogrlima.reservaai.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private CreateBookingService createBookingService;

    @Test
    void executeShouldBlockWhenUserAlreadyHasConfirmedBookingInPeriod() {
        CreateBookingRequestDTO request = request();

        when(bookingRepository.existsByUserIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.userId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(true);

        assertThatThrownBy(() -> createBookingService.execute(request))
                .isInstanceOf(UserAlreadyBookedException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void executeShouldBlockWhenRoomAlreadyHasConfirmedBookingInPeriod() {
        CreateBookingRequestDTO request = request();

        when(bookingRepository.existsByUserIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.userId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(false);
        when(bookingRepository.existsByRoomIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.roomId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(true);

        assertThatThrownBy(() -> createBookingService.execute(request))
                .isInstanceOf(BookingAlreadyExistsException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void executeShouldThrowWhenUserDoesNotExist() {
        CreateBookingRequestDTO request = request();

        when(bookingRepository.existsByUserIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.userId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(false);
        when(bookingRepository.existsByRoomIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.roomId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(false);
        when(userRepository.findById(request.userId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createBookingService.execute(request))
                .isInstanceOf(UserNotFoundException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void executeShouldThrowWhenRoomDoesNotExist() {
        CreateBookingRequestDTO request = request();
        User user = user();

        when(bookingRepository.existsByUserIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.userId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(false);
        when(bookingRepository.existsByRoomIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.roomId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(false);
        when(userRepository.findById(request.userId())).thenReturn(Optional.of(user));
        when(roomRepository.findById(request.roomId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createBookingService.execute(request))
                .isInstanceOf(RoomNotFoundException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void executeShouldSaveConfirmedBookingWithDatesAndEstimatedValue() {
        CreateBookingRequestDTO request = request();
        User user = user();
        Room room = room();
        Booking savedBooking = new Booking();
        BookingResponseDTO response = new BookingResponseDTO(
                30L,
                request.userId(),
                "Diego Lima",
                request.roomId(),
                "101",
                "CONFIRMED",
                request.checkIn(),
                request.checkOut(),
                new BigDecimal("600.00")
        );

        when(bookingRepository.existsByUserIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.userId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(false);
        when(bookingRepository.existsByRoomIdAndStatusAndCheckInLessThanEqualAndCheckOutGreaterThanEqual(
                request.roomId(),
                BookingStatus.CONFIRMED,
                request.checkOut(),
                request.checkIn()
        )).thenReturn(false);
        when(userRepository.findById(request.userId())).thenReturn(Optional.of(user));
        when(roomRepository.findById(request.roomId())).thenReturn(Optional.of(room));
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);
        when(bookingMapper.toResponse(savedBooking)).thenReturn(response);

        BookingResponseDTO result = createBookingService.execute(request);

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).save(bookingCaptor.capture());

        Booking booking = bookingCaptor.getValue();
        assertThat(booking.getUser()).isSameAs(user);
        assertThat(booking.getRoom()).isSameAs(room);
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(booking.getCheckIn()).isEqualTo(request.checkIn());
        assertThat(booking.getCheckOut()).isEqualTo(request.checkOut());
        assertThat(booking.getTotalEstimatedValue()).isEqualByComparingTo("600.00");
        assertThat(result).isSameAs(response);
    }

    private CreateBookingRequestDTO request() {
        return new CreateBookingRequestDTO(
                1L,
                10L,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(4)
        );
    }

    private User user() {
        User user = new User();
        user.setId(1L);
        user.setName("Diego Lima");
        user.setEmail("diego@example.com");
        user.setPassword("secret");
        return user;
    }

    private Room room() {
        Room room = new Room();
        room.setId(10L);
        room.setRoomNumber("101");
        room.setDailyRate(new BigDecimal("200.00"));
        return room;
    }
}
