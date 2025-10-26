package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static Booking mapToBooking(NewBookingDto request) {

        Booking booking = new Booking();
        booking.setStatus(Status.WAITING);
        booking.setStart(request.getStart());
        booking.setEnd(request.getEnd());
        booking.setBooker(request.getBookerObject());
        booking.setItem(request.getItemObject());

        return booking;
    }

    public static BookingDto mapToBookingDto(Booking booking) {

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setItem(booking.getItem());
        dto.setBooker(booking.getBooker());
        dto.setId(booking.getId());

        return dto;
    }
}
