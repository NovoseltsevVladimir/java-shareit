package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Setter
@Getter
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
}
