package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor
@Getter
@Setter
public class NewItemDto {
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private Long requestId;
}
