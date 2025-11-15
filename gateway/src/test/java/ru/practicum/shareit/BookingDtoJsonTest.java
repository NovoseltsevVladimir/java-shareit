package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingDtoJsonTest {
    private final JacksonTester<BookingDto> jsonDto;

    @Test
    void testBookingDto() throws Exception {
        BookingDto dto = new BookingDto();
        dto.setId(1L);
        dto.setStatus(Status.APPROVED);
        dto.setStart(LocalDateTime.now().plusDays(1));
        dto.setEnd(LocalDateTime.now().plusDays(2));
        dto.setItemId(2L);
        dto.setBookerId(3L);

        JsonContent<BookingDto> result = jsonDto.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(dto.getBookerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(dto.getItemId().intValue());

        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(dto.getStatus().toString());
    }

}