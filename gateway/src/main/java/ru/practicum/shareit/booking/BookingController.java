package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import static ru.practicum.shareit.ShareitConstants.USER_ID_HEADER_NAME;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody NewBookingDto bookingDto,
                                         @RequestHeader(USER_ID_HEADER_NAME) Long bookerId) {
        bookingDto.setBookerId(bookerId);
        return bookingClient.create(bookerId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> changeBookingStatus(@PathVariable(name = "bookingId") Long bookingId,
                                                      @RequestParam(name = "approved") boolean approved,
                                                      @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return bookingClient.changeBookingStatus(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingInformation(@PathVariable(name = "bookingId") Long bookingId,
                                                        @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return bookingClient.getBookingInformation(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookersBookingList(
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return bookingClient.getBookersBookingList(state.toString(), userId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnersBookingList(
            @RequestParam(name = "state", defaultValue = "ALL") BookingState state,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return bookingClient.getOwnersBookingList(state.toString(), userId);
    }
}
