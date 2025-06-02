package com.stc.rentalplatform.restcontroller;

import com.stc.rentalplatform.dto.BookingDTO;
import com.stc.rentalplatform.entity.Booking;
import com.stc.rentalplatform.exception.BookingConflictException;
import com.stc.rentalplatform.mapper.BookingMapper;
import com.stc.rentalplatform.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingMapper bookingMapper;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> getAll() {
        return bookingService.getAll();
    }
    @PostMapping("/createNewBooking")
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO dto) throws BookingConflictException {
        Booking booking = bookingMapper.toEntity(dto);
        Booking saved = bookingService.createBooking(booking);
        return new ResponseEntity<>(bookingMapper.toDto(saved), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookingService.deleteById(id);
    }
}
