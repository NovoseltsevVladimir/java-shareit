package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceIntegrationTest {

    private final EntityManager em;
    private final BookingService service;
    private final UserService userService;
    private final ItemServiceImpl itemService;

    private NewUserDto newUserDtoBooker;
    private NewUserDto newItemDtoOwner;
    private NewItemDto newItemDto;

    @BeforeEach
    void setUp() {

        newUserDtoBooker = new NewUserDto();
        newUserDtoBooker.setEmail("petya@mail.com");
        newUserDtoBooker.setName("Petya");

        newItemDtoOwner = new NewUserDto();
        newItemDtoOwner.setEmail("vasya@mail.com");
        newItemDtoOwner.setName("Vasya");
    }

    @Test
    void getAllBookings() {

        UserDto userDtoBooker = userService.create(newUserDtoBooker);
        UserDto userDtoOwner = userService.create(newItemDtoOwner);

        User userOwner = userService.findUserById(userDtoOwner.getId());
        User userbooker = userService.findUserById(userDtoBooker.getId());

        newItemDto = new NewItemDto();
        newItemDto.setName("Молоток");
        newItemDto.setDescription("Молоток хороший");
        newItemDto.setAvailable(true);
        newItemDto.setOwner(userOwner);

        ItemDto itemDto = itemService.create(newItemDto, userOwner.getId());
        Item item = itemService.findAvaliableItemById(itemDto.getId());

        LocalDateTime now = LocalDateTime.now();

        List<NewBookingDto> sourceDto = List.of(
                makeNewDto(userDtoBooker.getId(), itemDto.getId(), now, now.plusDays(1)),
                makeNewDto(userDtoBooker.getId(), itemDto.getId(), now.plusDays(2), now.plusDays(3)),
                makeNewDto(userDtoBooker.getId(), itemDto.getId(), now.plusDays(4), now.plusDays(5))
        );

        for (NewBookingDto request : sourceDto) {
            Booking entity = BookingMapper.mapToBooking(request, userbooker, item);
            em.persist(entity);
        }
        em.flush();

        Collection<BookingDto> targetDto = service.getOwnersBookingList(userOwner.getId(), State.ALL);

        assertThat(targetDto, hasSize(targetDto.size()));
        for (BookingDto bookingDto : targetDto) {
            assertThat(targetDto, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("item", equalTo(bookingDto.getItem())),
                    hasProperty("start", equalTo(bookingDto.getStart())),
                    hasProperty("end", equalTo(bookingDto.getEnd())),
                    hasProperty("status", equalTo(bookingDto.getStatus())),
                    hasProperty("booker", equalTo(bookingDto.getBooker()))
            )));
        }
    }

    private NewBookingDto makeNewDto(Long bookerId, Long itemId, LocalDateTime start, LocalDateTime end) {

        NewBookingDto dto = new NewBookingDto();
        dto.setBookerId(bookerId);
        dto.setItemId(itemId);
        dto.setStart(start);
        dto.setEnd(end);

        return dto;
    }
}