package ru.practicum.shareit.request;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceIntegrationTest {

    private final EntityManager em;
    private final ItemRequestService service;
    private final UserService userService;
    private NewUserDto newUserDto1;
    private NewUserDto newUserDto2;

    @BeforeEach
    void setUp() {

        newUserDto1 = new NewUserDto();
        newUserDto1.setEmail("petya@mail.com");
        newUserDto1.setName("Petya");

        newUserDto2 = new NewUserDto();
        newUserDto2.setEmail("vasya@mail.com");
        newUserDto2.setName("Vasya");

    }

    @Test
    void getAllItemRequests() {

        UserDto userDto1 = userService.create(newUserDto1);
        UserDto userDto2 = userService.create(newUserDto2);

        List<NewItemRequestDto> sourceDto = List.of(
                makeNewDto("Молоток", userDto1.getId()),
                makeNewDto("Гвозди", userDto2.getId()),
                makeNewDto("Пылесос", userDto2.getId())
        );

        for (NewItemRequestDto request : sourceDto) {
            ItemRequest entity = ItemRequestMapper.mapToItemRequest(request,
                    userService.findUserById(request.getRequestor()));
            em.persist(entity);
        }
        em.flush();

        Collection<ItemRequestDto> targetUsers = service.getAllWithoutCurrentUser(userDto1.getId());

        assertThat(targetUsers, hasSize(targetUsers.size()));
        for (ItemRequestDto itemRequestDto : targetUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(itemRequestDto.getDescription())),
                    hasProperty("requestor", equalTo(itemRequestDto.getRequestor()))
            )));
        }
    }

    private NewItemRequestDto makeNewDto(String description, Long idRequestor) {

        NewItemRequestDto dto = new NewItemRequestDto();
        dto.setDescription(description);
        dto.setRequestor(idRequestor);

        return dto;
    }
}