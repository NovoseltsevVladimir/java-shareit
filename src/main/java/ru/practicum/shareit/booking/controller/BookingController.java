package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareitConstants;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

import static ru.practicum.shareit.booking.Status.APPROVED;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController implements ShareitConstants {

    BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto create(@Valid @RequestBody NewBookingDto bookingDto,
                             @RequestHeader(USER_ID_HEADER_NAME) long bookerId) {

        bookingDto.setBookerId(bookerId);

        return bookingService.create(bookingDto);
    }

    @PatchMapping ("/{bookingId}")
    public BookingDto changeBookingStatus(@PathVariable(name = "bookingId") Long bookingId,
            @RequestParam (name = "approved") boolean approved,
            @RequestHeader(USER_ID_HEADER_NAME) long userId) {

        Status newStatus = approved ? APPROVED: Status.REJECTED;
        return bookingService.changeStatus(bookingId,newStatus, userId);

    }

    //GET /bookings/{bookingId}
    @GetMapping ("/{bookingId}")
    public BookingDto getBookingInformation (@PathVariable(name = "bookingId") Long bookingId,
                                             @RequestHeader(USER_ID_HEADER_NAME) long userId) {

        return bookingService.findById(bookingId,userId);
    }

    //GET /bookings?state={state}
    @GetMapping
    public Collection<BookingDto> getBookersBookingList(
            @RequestParam(name = "state", defaultValue = "ALL") State state,
            @RequestHeader(USER_ID_HEADER_NAME) long userId) {

        return bookingService.getBookersBookingList(userId,state);
    }

    //GET /bookings/owner?state={state}
    @GetMapping ("/owner")
    public Collection<BookingDto> getOwnersBookingList(
            @RequestParam(name = "state", defaultValue = "ALL") State state,
            @RequestHeader(USER_ID_HEADER_NAME) long userId) {

        return bookingService.getOwnersBookingList(userId,state);
    }

}
