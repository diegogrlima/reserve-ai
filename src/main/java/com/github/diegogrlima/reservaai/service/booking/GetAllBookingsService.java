package com.github.diegogrlima.reservaai.service.booking;

import com.github.diegogrlima.reservaai.dto.response.BookingResponseDTO;
import com.github.diegogrlima.reservaai.mapper.BookingMapper;
import com.github.diegogrlima.reservaai.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllBookingsService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;


    public Page<BookingResponseDTO> execute(Pageable pageable){
        log.debug("Listando reservas page={} size={} sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        return bookingRepository
                .findAll(pageable)
                .map(bookingMapper::toResponse);

    }
}
