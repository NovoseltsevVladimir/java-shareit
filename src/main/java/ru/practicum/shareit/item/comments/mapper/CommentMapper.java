package ru.practicum.shareit.item.comments.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.NewCommentDto;
import ru.practicum.shareit.item.comments.model.Comment;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public static Comment mapToComment(NewCommentDto request) {

        Comment comment = new Comment();
        comment.setItem(request.getItem());
        comment.setAuthor(request.getAuthor());
        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());

        return comment;
    }

    public static CommentDto mapToCommentDto(Comment comment) {

        CommentDto dto = new CommentDto();
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setId(comment.getId());
        dto.setItem(comment.getItem());
        dto.setText(comment.getText());
        dto.setCreated(comment.getCreated());

        return dto;
    }

}
