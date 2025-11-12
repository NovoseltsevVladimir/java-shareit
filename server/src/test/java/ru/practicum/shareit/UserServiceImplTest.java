package ru.practicum.shareit;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:postgresql://localhost:5432/test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final EntityManager em;
    private final UserService service;

    @Test
    void createUser() {
        // given
        NewUserDto userDto = makeUserDto("petya@mail.com", "Petya");

        // when
        service.create(userDto);

        // then
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateUser() {

        NewUserDto userDto = makeUserDto("petya@mail.com", "Petya");

        UserDto newUser = service.create(userDto);

        UpdateUserDto userDtoForUpdate = new UpdateUserDto();
        userDtoForUpdate.setId(newUser.getId());
        userDtoForUpdate.setName("Vasya");

        service.update(userDtoForUpdate);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), equalTo(userDtoForUpdate.getId()));
        assertThat(user.getName(), equalTo(userDtoForUpdate.getName()));
        assertThat(user.getEmail(), equalTo(newUser.getEmail()));
    }

    @Test
    void deleteUser() {

        NewUserDto userDto = makeUserDto("petya@mail.com", "Petya");
        UserDto newUser = service.create(userDto);

        service.delete(newUser.getId());

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        try {
            User user = query.setParameter("id", newUser.getId())
                    .getSingleResult();
            assertThat(0, equalTo(1)); // результатов быть не должно
        } catch (NoResultException exp) {
            assertThat(1, equalTo(1));
        }
    }

    @Test
    void getAllUsers() {
        // given
        List<NewUserDto> sourceUsers = List.of(
                makeUserDto("petya@mail.com", "Petya"),
                makeUserDto("vanya@mail.com", "Vanya"),
                makeUserDto("sasha@mail.com", "Sasha")
        );

        for (NewUserDto user : sourceUsers) {
            User entity = UserMapper.mapToUser(user);
            em.persist(entity);
        }
        em.flush();

        // when
        Collection<UserDto> targetUsers = service.findAll();

        // then
        assertThat(targetUsers, hasSize(sourceUsers.size()));
        for (NewUserDto sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    void getUserById() {
        // given
        NewUserDto newUserDto = makeUserDto("petya@mail.com", "Petya");

        // when
        UserDto userDto = service.create(newUserDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", userDto.getId())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));


    }

    private NewUserDto makeUserDto(String email, String name) {

        NewUserDto dto = new NewUserDto();
        dto.setEmail(email);
        dto.setName(name);

        return dto;
    }
}