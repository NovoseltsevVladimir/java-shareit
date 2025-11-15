package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ShareitConstants.USER_ID_HEADER_NAME;

@ExtendWith(MockitoExtension.class)
class BookingControllerMockMvcTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private BookingDto bookingDto;
    private BookingDto bookingDtoForList;
    private User user;
    private Item item;

    private String path = "/bookings";

    @BeforeEach
    void setUp() {

        mapper.registerModule(new JavaTimeModule());

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        user = new User();
        user.setName("Petya");
        user.setEmail("petya@mail.com");
        user.setId(1L);

        item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setDescription("lalala");
        item.setComments(new ArrayList<>());

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBooker(user);
        bookingDto.setItem(item);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));
        bookingDto.setStatus(Status.WAITING);

        bookingDtoForList = new BookingDto();
        bookingDtoForList.setId(2L);
        bookingDtoForList.setBooker(user);
        bookingDtoForList.setItem(item);
        bookingDtoForList.setStart(LocalDateTime.now().minusDays(1));
        bookingDtoForList.setEnd(LocalDateTime.now().plusDays(2));
        bookingDtoForList.setStatus(Status.WAITING);
    }

    @Test
    void createBooking() throws Exception {
        when(bookingService.create(any()))
                .thenReturn(bookingDto);

        mvc.perform(post(path)
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.end", is(notNullValue())))
                .andExpect(jsonPath("$.item.description", is(item.getDescription())))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class));
    }

    @Test
    void changeBookingStatus() throws Exception {
        bookingDto.setStatus(Status.APPROVED);
        when(bookingService.changeStatus(anyLong(), any(Status.class), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(patch(path + "/" + bookingDto.getId() + "?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())));
    }

    @Test
    void findBookingById() throws Exception {
        when(bookingService.findById(bookingDto.getId(), user.getId()))
                .thenReturn(bookingDto);

        mvc.perform(get(path + "/" + +bookingDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.start", is(notNullValue())))
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.end", is(notNullValue())))
                .andExpect(jsonPath("$.item.description", is(item.getDescription())))
                .andExpect(jsonPath("$.item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(user.getId()), Long.class));
    }

    @Test
    void getBookersBookingList() throws Exception {
        List<BookingDto> dtoList = new ArrayList<>();
        dtoList.add(bookingDto);
        dtoList.add(bookingDtoForList);

        when(bookingService.getBookersBookingList(user.getId(), State.ALL))
                .thenReturn(dtoList);

        mvc.perform(get(path)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start").exists())
                .andExpect(jsonPath("$[0].start", is(notNullValue())))
                .andExpect(jsonPath("$[0].end").exists())
                .andExpect(jsonPath("$[0].end", is(notNullValue())))
                .andExpect(jsonPath("$[0].item.description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(bookingDtoForList.getId()), Long.class))
                .andExpect(jsonPath("$[1].start").exists())
                .andExpect(jsonPath("$[1].start", is(notNullValue())))
                .andExpect(jsonPath("$[1].end").exists())
                .andExpect(jsonPath("$[1].end", is(notNullValue())))
                .andExpect(jsonPath("$[1].item.description", is(item.getDescription())))
                .andExpect(jsonPath("$[1].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[1].booker.id", is(user.getId()), Long.class));
    }

    @Test
    void getOwnersBookingList() throws Exception {
        List<BookingDto> dtoList = new ArrayList<>();
        dtoList.add(bookingDto);

        when(bookingService.getOwnersBookingList(user.getId(), State.ALL))
                .thenReturn(dtoList);

        mvc.perform(get(path + "/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start").exists())
                .andExpect(jsonPath("$[0].start", is(notNullValue())))
                .andExpect(jsonPath("$[0].end").exists())
                .andExpect(jsonPath("$[0].end", is(notNullValue())))
                .andExpect(jsonPath("$[0].item.description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].item.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(user.getId()), Long.class));
    }
}