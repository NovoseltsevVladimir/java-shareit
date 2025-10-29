package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long>,
        QuerydslPredicateExecutor<Booking> {

    @Query("SELECT MIN(b.start) FROM Booking b " +
            "WHERE b.item = ?1 AND b.start >= ?2 AND b.status = ?3")
    LocalDateTime findNextBookingDate(Item item, LocalDateTime now, Status status);

    @Query("SELECT MAX(b.end) FROM Booking b " +
            "WHERE b.item = ?1 AND b.end < ?2 AND b.status = ?3")
    LocalDateTime findPastBookingDate(Item item, LocalDateTime now, Status status);

    @Query("SELECT b FROM Booking b " +
                "WHERE b.item = ?1 AND b.status = ?2 AND b.booker = ?3 AND b.end<?4")
    List<Booking> getItemBookings (Item item, Status status, User booker, LocalDateTime currentDate);
}
