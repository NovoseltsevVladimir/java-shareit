package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    @NotBlank
    private String text;
    @NotNull
    private Long itemId;
    @NotBlank
    private String authorName;
    @PastOrPresent
    private LocalDateTime created;
}
