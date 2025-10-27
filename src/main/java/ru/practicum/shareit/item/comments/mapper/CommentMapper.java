package ru.practicum.shareit.item.comments.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.NewCommentDto;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;

@Component
public class CommentMapper {

    public static Comment mapToComment(NewCommentDto request) {

        Comment comment = new Comment();
        comment.setItem(request.getItem());
        comment.setAuthor(request.getAuthor());
        comment.setText(request.getText());

        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {

        CommentDto dto = new CommentDto();
        dto.setAuthor(comment.getAuthor());
        dto.setId(comment.getId());
        dto.setItem(comment.getItem());
        dto.setText(comment.getText());
        dto.setCreated(comment.getCreated());

        return dto;
    }

}
