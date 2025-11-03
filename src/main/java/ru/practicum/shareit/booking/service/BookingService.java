package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingService {

    BookingRepository repository;
    UserService userService;
    ItemService itemService;

    @Autowired
    public BookingService(BookingRepository repository, UserService userService, ItemService itemService) {
        this.repository = repository;
        this.userService = userService;
        this.itemService = itemService;
    }

    public BookingDto create(NewBookingDto bookingDto) {

        User booker = userService.findUserById(bookingDto.getBookerId());
        Item item = itemService.findAvaliableItemById(bookingDto.getItemId());

        Booking newBooking = BookingMapper.mapToBooking(bookingDto, booker, item);

        newBooking = repository.save(newBooking);

        return BookingMapper.mapToBookingDto(newBooking);
    }

    public BookingDto changeStatus(Long bookingId, Status newStatus, Long userId) {
        Optional<Booking> optionalBooking = repository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            String bugText = "Бронирование не найдено, id " + bookingId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        Booking booking = optionalBooking.get();

        //Менять статус разрешено только владельцу вещи
        if (!isUserItemOwner(booking, userId)) {
            String bugText = "Доступ запрещен, id пользователя " + userId + " id бронирования " + bookingId;
            log.warn(bugText);
            throw new ForbiddenException(bugText);
        }

        if (booking.getStatus() != newStatus) {
            booking.setStatus(newStatus);
            booking = repository.save(booking);
        }

        return BookingMapper.mapToBookingDto(booking);

    }

    public BookingDto findById(Long bookingId, Long userId) {

        Optional<Booking> optionalBooking = repository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            String bugText = "Бронирование не найдено, id " + bookingId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        Booking booking = optionalBooking.get();
        if (isUserBooker(booking, userId) || isUserItemOwner(booking, userId)) {
            return BookingMapper.mapToBookingDto(booking);
        } else {
            String bugText = "Доступ запрещен, id пользователя " + userId + " id бронирования " + bookingId;
            log.warn(bugText);
            throw new ForbiddenException(bugText);
        }

    }

    private boolean isUserItemOwner(Booking booking, Long userId) {
        Item item = booking.getItem();
        return item.getOwner().getId().equals(userId);
    }

    private boolean isUserBooker(Booking booking, Long userId) {
        return booking.getBooker().getId().equals(userId);
    }

    public Collection<BookingDto> getBookersBookingList(Long userId, State state) {
        if (userService.findUserById(userId) == null) {
            String bugText = "Пользователь не найден, id " + userId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }
        return findByState(userId, state, true);
    }

    public Collection<BookingDto> getOwnersBookingList(Long userId, State state) {
        if (userService.findUserById(userId) == null) {
            String bugText = "Пользователь не найден, id " + userId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }
        return findByState(userId, state, false);
    }

    private Collection<BookingDto> findByState(Long userId, State state, boolean byBooker) {

        Iterable<Booking> bookings;
        BooleanExpression byUserId;
        if (byBooker) {
            byUserId = QBooking.booking.booker.id.eq(userId);
        } else {
            //owner
            byUserId = QBooking.booking.item.owner.id.eq(userId);
        }

        LocalDateTime currentDateTime = LocalDateTime.now();

        if (state == State.ALL) {
            bookings = repository.findAll(byUserId);
        } else if (state == State.PAST) {
            BooleanExpression inPast = QBooking.booking.end.before(currentDateTime);
            bookings = repository.findAll(byUserId.and(inPast));
        } else if (state == State.FUTURE) {
            BooleanExpression inFuture = QBooking.booking.start.after(currentDateTime);
            bookings = repository.findAll(byUserId.and(inFuture));
        } else if (state == State.WAITING) {
            BooleanExpression isWaiting = QBooking.booking.status.eq(Status.WAITING);
            bookings = repository.findAll(byUserId.and(isWaiting));
        } else if (state == State.CURRENT) {
            BooleanExpression betweenEnd = QBooking.booking.end.after(currentDateTime);
            BooleanExpression betweenStart = QBooking.booking.start.before(currentDateTime);
            bookings = repository.findAll(byUserId.and(betweenStart.and(betweenStart)));
        } else {
            throw new NotFoundException("Вид сортировки " + state + " не найден");
        }

        Collection<Booking> bookingsCollection = new ArrayList<>();
        bookings.forEach(bookingsCollection::add);

        return bookingsCollection
                .stream()
                .map(booking -> BookingMapper.mapToBookingDto(booking))
                .collect(Collectors.toList());
    }

}
