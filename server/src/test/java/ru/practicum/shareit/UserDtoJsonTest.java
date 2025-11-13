package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

//Для всех dto с валидацией. Имеет смысл только в gateway

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoJsonTest {
    private final JacksonTester<UserDto> json;

//    @Test
//    void testUserDto() throws Exception {
//        UserDto userDto = new UserDto(
//                1L,
//                "john.doe@mail.com",
//                "John",
//                "Doe",
//                "2022.07.03 19:55:00",
//                UserState.ACTIVE);
//
//        JsonContent<UserDto> result = json.write(userDto);
//
//        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
//        assertThat(result).extractingJsonPathStringValue("$.firstName").isEqualTo("John");
//        assertThat(result).extractingJsonPathStringValue("$.lastName").isEqualTo("Doe");
//        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
//    }
} 