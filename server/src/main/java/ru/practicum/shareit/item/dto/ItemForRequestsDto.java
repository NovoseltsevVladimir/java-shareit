package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ItemForRequestsDto {
    private Long id;
    private Long requestId;
    private Long ownerId;
    private String name;
}
