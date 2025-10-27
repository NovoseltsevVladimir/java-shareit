package ru.practicum.shareit.item.comments.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
public class NewCommentDto {
    private String text;
    private Long itemId;
    private Long authorId;
    private Item item;
    private User author;
}
