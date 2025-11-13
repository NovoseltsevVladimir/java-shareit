package ru.practicum.shareit.request;

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
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
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

//MockMvc тестирование
//Для каждого эндпоинта

@ExtendWith(MockitoExtension.class)
class RequestControllerMockMvcTest {
    @Mock
    private ItemRequestService itemRequestService;

    @InjectMocks
    private ItemRequestController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemRequestDto itemRequestDto1;
    private ItemRequestDto itemRequestDto2;
    private UserDto userDto;

    @BeforeEach
    void setUp() {

        mapper.registerModule(new JavaTimeModule());

        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        userDto = new UserDto();
        userDto.setName("Petya");
        userDto.setEmail("petya@mail.com");
        userDto.setId(1L);

        itemRequestDto1 = new ItemRequestDto();
        itemRequestDto1.setId(1L);
        itemRequestDto1.setCreated(LocalDateTime.now());
        itemRequestDto1.setRequestor(userDto);
        itemRequestDto1.setDescription("tututu");

        itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setId(2L);
        itemRequestDto2.setCreated(LocalDateTime.now().minusHours(1));
        itemRequestDto2.setRequestor(userDto);
        itemRequestDto2.setDescription("lalala");
    }

    @Test
    void createItemRequest() throws Exception {
        when(itemRequestService.create(any(), anyLong()))
                .thenReturn(itemRequestDto1);

        NewItemRequestDto newItemRequest = new NewItemRequestDto();
        newItemRequest.setDescription(itemRequestDto1.getDescription());
        newItemRequest.setRequestor(itemRequestDto1.getRequestor().getId());

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(newItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(itemRequestDto1.getRequestor().getId().intValue())))
                .andExpect(jsonPath("$.requestor.name", is(itemRequestDto1.getRequestor().getName())))
                .andExpect(jsonPath("$.requestor.email", is(itemRequestDto1.getRequestor().getEmail())))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.created", is(notNullValue())));
        ;
    }

    @Test
    void findById() throws Exception {

        ItemRequestDtoWithAnswers requestDtoWithAnswers = new ItemRequestDtoWithAnswers();
        requestDtoWithAnswers.setDescription(itemRequestDto1.getDescription());
        requestDtoWithAnswers.setCreated(itemRequestDto1.getCreated());
        requestDtoWithAnswers.setRequestor(itemRequestDto1.getRequestor());
        requestDtoWithAnswers.setId(itemRequestDto1.getId());

        when(itemRequestService.getById(itemRequestDto1.getId()))
                .thenReturn(requestDtoWithAnswers);

        mvc.perform(get("/requests/" + requestDtoWithAnswers.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoWithAnswers.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDtoWithAnswers.getDescription())))
                .andExpect(jsonPath("$.requestor.id", is(requestDtoWithAnswers.getRequestor().getId().intValue())))
                .andExpect(jsonPath("$.requestor.name", is(requestDtoWithAnswers.getRequestor().getName())))
                .andExpect(jsonPath("$.requestor.email", is(requestDtoWithAnswers.getRequestor().getEmail())))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.created", is(notNullValue())));
        ;
    }

    @Test
    void getAllWithAnswers() throws Exception {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto1.getId());
        itemRequest.setRequestor(user);
        itemRequest.setDescription(itemRequestDto1.getDescription());

        Item item1 = new Item();
        item1.setName("lala");
        item1.setOwner(user);
        item1.setRequest(itemRequest);

        Item item2 = new Item();
        item2.setName("tutu");
        item2.setOwner(user);
        item2.setRequest(itemRequest);

        ItemRequestDtoWithAnswers requestDtoWithAnswers1 = new ItemRequestDtoWithAnswers();
        requestDtoWithAnswers1.setDescription(itemRequestDto1.getDescription());
        requestDtoWithAnswers1.setCreated(itemRequestDto1.getCreated());
        requestDtoWithAnswers1.setRequestor(itemRequestDto1.getRequestor());
        requestDtoWithAnswers1.setId(itemRequestDto1.getId());
        requestDtoWithAnswers1.setItems(List.of(ItemMapper.mapToItemForRequestsDto(item1),
                ItemMapper.mapToItemForRequestsDto(item2)));

        ItemRequestDtoWithAnswers requestDtoWithAnswers2 = new ItemRequestDtoWithAnswers();
        requestDtoWithAnswers2.setDescription(itemRequestDto2.getDescription());
        requestDtoWithAnswers2.setCreated(itemRequestDto2.getCreated());
        requestDtoWithAnswers2.setRequestor(itemRequestDto2.getRequestor());
        requestDtoWithAnswers2.setId(itemRequestDto2.getId());
        requestDtoWithAnswers2.setItems(List.of(ItemMapper.mapToItemForRequestsDto(item1),
                ItemMapper.mapToItemForRequestsDto(item2)));

        List<ItemRequestDtoWithAnswers> dtoList = new ArrayList<>();
        dtoList.add(requestDtoWithAnswers1);
        dtoList.add(requestDtoWithAnswers2);

        when(itemRequestService.getAllWithAnswers(userDto.getId()))
                .thenReturn(dtoList);

        mvc.perform(get("/requests/")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, userDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(requestDtoWithAnswers1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDtoWithAnswers1.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(requestDtoWithAnswers1.getRequestor().getId().intValue())))
                .andExpect(jsonPath("$[0].requestor.name", is(requestDtoWithAnswers1.getRequestor().getName())))
                .andExpect(jsonPath("$[0].requestor.email", is(requestDtoWithAnswers1.getRequestor().getEmail())))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].created", is(notNullValue())))
                .andExpect(jsonPath("$[0].items[0].name", is(item1.getName())))
                .andExpect(jsonPath("$[0].items[1].name", is(item2.getName())))
                .andExpect(jsonPath("$[1].id", is(requestDtoWithAnswers2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(requestDtoWithAnswers2.getDescription())))
                .andExpect(jsonPath("$[1].requestor.id", is(requestDtoWithAnswers2.getRequestor().getId().intValue())))
                .andExpect(jsonPath("$[1].requestor.name", is(requestDtoWithAnswers2.getRequestor().getName())))
                .andExpect(jsonPath("$[1].requestor.email", is(requestDtoWithAnswers2.getRequestor().getEmail())))
                .andExpect(jsonPath("$[1].created").exists())
                .andExpect(jsonPath("$[1].created", is(notNullValue())))
                .andExpect(jsonPath("$[1].items[0].name", is(item1.getName())))
                .andExpect(jsonPath("$[1].items[1].name", is(item2.getName())));
    }

    @Test
    void getAllWithoutCurrentUser() throws Exception {

        List<ItemRequestDto> dtoList = new ArrayList<>();
        dtoList.add(itemRequestDto1);
        dtoList.add(itemRequestDto2);

        when(itemRequestService.getAllWithoutCurrentUser(userDto.getId()))
                .thenReturn(dtoList);

        mvc.perform(get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$[0].requestor.id", is(itemRequestDto1.getRequestor().getId().intValue())))
                .andExpect(jsonPath("$[0].requestor.name", is(itemRequestDto1.getRequestor().getName())))
                .andExpect(jsonPath("$[0].requestor.email", is(itemRequestDto1.getRequestor().getEmail())))
                .andExpect(jsonPath("$[0].created").exists())
                .andExpect(jsonPath("$[0].created", is(notNullValue())))
                .andExpect(jsonPath("$[1].id", is(itemRequestDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].description", is(itemRequestDto2.getDescription())))
                .andExpect(jsonPath("$[1].requestor.id", is(itemRequestDto2.getRequestor().getId().intValue())))
                .andExpect(jsonPath("$[1].requestor.name", is(itemRequestDto2.getRequestor().getName())))
                .andExpect(jsonPath("$[1].requestor.email", is(itemRequestDto2.getRequestor().getEmail())))
                .andExpect(jsonPath("$[1].created").exists())
                .andExpect(jsonPath("$[1].created", is(notNullValue())));
    }
}