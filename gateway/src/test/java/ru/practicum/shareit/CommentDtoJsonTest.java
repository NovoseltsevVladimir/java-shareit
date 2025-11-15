package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentDtoJsonTest {
    private final JacksonTester<CommentDto> jsonDto;

    @Test
    void testCommentDto() throws Exception {
        CommentDto dto = new CommentDto();
        dto.setText("Молоток отличный");
        dto.setAuthorName("Petya");
        dto.setCreated(LocalDateTime.now());
        dto.setId(1L);
        dto.setItemId(2L);

        JsonContent<CommentDto> result = jsonDto.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(dto.getItemId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(dto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(dto.getText());
    }

}