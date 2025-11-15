package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDtoJsonTest {
    private final JacksonTester<ItemDto> jsonDto;

    @Test
    void testItemDto() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setId(1L);
        dto.setDescription("Молоток хороший");
        dto.setName("Молоток");
        dto.setOwnerId(2L);
        dto.setRequestId(3L);
        dto.setNextBooking(LocalDateTime.now().plusDays(1));
        dto.setLastBooking(LocalDateTime.now().minusDays(1));

        JsonContent<ItemDto> result = jsonDto.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId")
                .isEqualTo(dto.getOwnerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(dto.getRequestId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(dto.getDescription());
    }

}