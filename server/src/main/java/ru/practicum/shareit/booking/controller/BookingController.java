package ru.practicum.shareit.booking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

import static ru.practicum.shareit.ShareitConstants.USER_ID_HEADER_NAME;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@RequestBody NewBookingDto bookingDto,
                             @RequestHeader(USER_ID_HEADER_NAME) Long bookerId) {

        bookingDto.setBookerId(bookerId);

        return bookingService.create(bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeBookingStatus(@PathVariable(name = "bookingId") Long bookingId,
                                          @RequestParam(name = "approved") boolean approved,
                                          @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        Status newStatus = approved ? Status.APPROVED : Status.REJECTED;
        return bookingService.changeStatus(bookingId, newStatus, userId);

    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInformation(@PathVariable(name = "bookingId") Long bookingId,
                                            @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return bookingService.findById(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> getBookersBookingList(
            @RequestParam(name = "state", defaultValue = "ALL") State state,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return bookingService.getBookersBookingList(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getOwnersBookingList(
            @RequestParam(name = "state", defaultValue = "ALL") State state,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return bookingService.getOwnersBookingList(userId, state);
    }
}
