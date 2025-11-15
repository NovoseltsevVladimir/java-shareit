package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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
class ItemServiceIntegrationTest {

    private final EntityManager em;
    private final ItemServiceImpl service;
    private final UserService userService;
    private NewUserDto newUserDto;

    @BeforeEach
    void setUp() {

        newUserDto = new NewUserDto();
        newUserDto.setEmail("petya@mail.com");
        newUserDto.setName("Petya");

    }

    @Test
    void getAllItems() {

        UserDto userDto = userService.create(newUserDto);
        User user = userService.findUserById(userDto.getId());

        List<NewItemDto> sourceDto = List.of(
                makeNewDto("Молоток", "Молоток хороший", true, user, null),
                makeNewDto("Гвозди", "Гвозди длинные", true, user, null),
                makeNewDto("Пылесос", "Пылесос мощный", true, user, null)
        );

        for (NewItemDto request : sourceDto) {
            Item entity = ItemMapper.mapToItem(request);
            em.persist(entity);
        }
        em.flush();

        Collection<ItemDto> targetDto = service.findAll(user.getId());

        assertThat(targetDto, hasSize(targetDto.size()));
        for (ItemDto itemRequestDto : targetDto) {
            assertThat(targetDto, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(itemRequestDto.getDescription())),
                    hasProperty("name", equalTo(itemRequestDto.getName())),
                    hasProperty("available", equalTo(itemRequestDto.getAvailable())),
                    hasProperty("owner", equalTo(itemRequestDto.getOwner())),
                    hasProperty("request", equalTo(itemRequestDto.getRequest()))

            )));
        }
    }

    private NewItemDto makeNewDto(String name, String description, boolean avaliable, User owner, Long requestId) {

        NewItemDto dto = new NewItemDto();
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(avaliable);
        dto.setOwner(owner);
        dto.setRequestId(requestId);

        return dto;
    }
}