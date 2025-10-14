package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

@Data
public class NewItemRequest {
    @NotBlank
    @NonNull
    private String name;
    @NotBlank
    @NonNull
    private String description;
    @NonNull
    private Boolean available;
    private Long owner;
    private String request;
}
