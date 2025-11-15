package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.mapper.CommentMapper;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
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
public class ItemControllerMockMvcTest {
    @Mock
    private ItemService service;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private ItemDto itemDto1;
    private ItemDto itemDto2;
    private User user;
    private Comment comment1;
    private Comment comment2;

    private String path = "/items";

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

        comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("pupupu");
        comment1.setAuthor(user);
        comment1.setCreated(LocalDateTime.now());

        comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("mumumu");
        comment2.setAuthor(user);
        comment2.setCreated(LocalDateTime.now());

        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        itemDto1 = new ItemDto();
        itemDto1.setId(1L);
        itemDto1.setOwner(user);
        itemDto1.setDescription("lalalalalala");
        itemDto1.setAvaliable(true);
        itemDto1.setName("lalala");
        itemDto1.setComments(comments);

        itemDto2 = new ItemDto();
        itemDto2.setId(2L);
        itemDto2.setOwner(user);
        itemDto2.setDescription("tutututututu");
        itemDto2.setAvaliable(true);
        itemDto2.setName("tututu");
        itemDto2.setComments(comments);

    }

    @Test
    void createItem() throws Exception {
        when(service.create(any(), anyLong()))
                .thenReturn(itemDto1);

        mvc.perform(post(path)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.owner.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].id", is(comment1.getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].text", is(comment1.getText())))
                .andExpect(jsonPath("$.comments[1].id", is(comment2.getId()), Long.class))
                .andExpect(jsonPath("$.comments[1].text", is(comment2.getText())));
    }

    @Test
    void updateItem() throws Exception {

        itemDto1.setName("lilili");
        itemDto1.setDescription("riririr");

        when(service.update(any(), anyLong()))
                .thenReturn(itemDto1);

        mvc.perform(patch(path + "/" + itemDto1.getId())
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.owner.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].id", is(comment1.getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].text", is(comment1.getText())))
                .andExpect(jsonPath("$.comments[1].id", is(comment2.getId()), Long.class))
                .andExpect(jsonPath("$.comments[1].text", is(comment2.getText())));
    }

    @Test
    void deleteItem() throws Exception {

        when(service.delete(any(), anyLong()))
                .thenReturn(itemDto1);

        mvc.perform(delete(path + "/" + itemDto1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void findById() throws Exception {

        when(service.findById(any(), anyLong()))
                .thenReturn(itemDto1);

        mvc.perform(get(path + "/" + itemDto1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.owner.id", is(user.getId()), Long.class))
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].id", is(comment1.getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].text", is(comment1.getText())))
                .andExpect(jsonPath("$.comments[1].id", is(comment2.getId()), Long.class))
                .andExpect(jsonPath("$.comments[1].text", is(comment2.getText())));
    }

    @Test
    void createComment() throws Exception {
        CommentDto commentDto = CommentMapper.mapToCommentDto(comment1);

        when(service.createComment(any()))
                .thenReturn(commentDto);

        mvc.perform(post(path + "/" + itemDto1.getId() + "/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(user.getName())))
                .andExpect(jsonPath("$.created").exists())
                .andExpect(jsonPath("$.created", is(notNullValue())));
    }


    @Test
    void findAll() throws Exception {

        List<ItemDto> dtoList = new ArrayList<>();
        dtoList.add(itemDto1);
        dtoList.add(itemDto2);

        when(service.findAll(anyLong()))
                .thenReturn(dtoList);

        mvc.perform(get(path)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$[0].owner.id", is(itemDto1.getOwner().getId()), Long.class))
                .andExpect(jsonPath("$[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].comments[0].id", is(comment1.getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[0].text", is(comment1.getText())))
                .andExpect(jsonPath("$[0].comments[1].id", is(comment2.getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[1].text", is(comment2.getText())))
                .andExpect(jsonPath("$[1].id", is(itemDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(itemDto2.getName())))
                .andExpect(jsonPath("$[1].description", is(itemDto2.getDescription())))
                .andExpect(jsonPath("$[1].owner.id", is(itemDto2.getOwner().getId()), Long.class))
                .andExpect(jsonPath("$[1].comments", hasSize(2)))
                .andExpect(jsonPath("$[1].comments[0].id", is(comment1.getId()), Long.class))
                .andExpect(jsonPath("$[1].comments[0].text", is(comment1.getText())))
                .andExpect(jsonPath("$[1].comments[1].id", is(comment2.getId()), Long.class))
                .andExpect(jsonPath("$[1].comments[1].text", is(comment2.getText())));
    }

    @Test
    void findAvaliableItemByText() throws Exception {

        List<ItemDto> dtoList = new ArrayList<>();
        dtoList.add(itemDto1);

        String textForSearch = "lalala";

        when(service.findAvaliableItemByText(textForSearch, user.getId()))
                .thenReturn(dtoList);

        mvc.perform(get(path + "/search?text=" + textForSearch)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_ID_HEADER_NAME, user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$[0].owner.id", is(itemDto1.getOwner().getId()), Long.class))
                .andExpect(jsonPath("$[0].comments", hasSize(2)))
                .andExpect(jsonPath("$[0].comments[0].id", is(comment1.getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[0].text", is(comment1.getText())))
                .andExpect(jsonPath("$[0].comments[1].id", is(comment2.getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[1].text", is(comment2.getText())));
    }
}
