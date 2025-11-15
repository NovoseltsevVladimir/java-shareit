package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoRequestJsonTest {
    private final JacksonTester<ItemRequestDto> jsonDto;

    @Test
    void testItemRequestDto() throws Exception {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Нужен молоток");
        dto.setId(1L);
        dto.setCreated(LocalDateTime.now());
        dto.setRequestorId(2L);

        JsonContent<ItemRequestDto> result = jsonDto.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(dto.getCreated().toString());
        assertThat(result).extractingJsonPathNumberValue("$.requestorId")
                .isEqualTo(dto.getRequestorId().intValue());
    }

}
