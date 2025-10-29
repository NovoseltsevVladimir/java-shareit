package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private Set<Comment> comments = new HashSet<>();
}
