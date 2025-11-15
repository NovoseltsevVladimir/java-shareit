package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class NewItemRequestDto {

    private String description;
    private Long requestor;

}
