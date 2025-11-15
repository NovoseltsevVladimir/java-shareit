package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewItemDto {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}
