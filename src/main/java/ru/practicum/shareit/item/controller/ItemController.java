package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.ShareitConstants;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController implements ShareitConstants {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader(USER_ID_HEADER_NAME) long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable long itemId) {
        return itemService.findById(itemId);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody NewItemDto item,
                          @RequestHeader(USER_ID_HEADER_NAME) long userId) {
        return itemService.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Valid @RequestBody UpdateItemDto newItem,
                          @PathVariable("itemId") Long itemId,
                          @RequestHeader(USER_ID_HEADER_NAME) long userId) {
        newItem.setId(itemId);
        return itemService.update(newItem, userId);
    }

    @DeleteMapping("/{itemId}")
    public ItemDto deleteItem(@PathVariable("id") Long itemId,
                              @RequestHeader(USER_ID_HEADER_NAME) long userId) {

        return itemService.delete(itemId, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findAvaliableItemByText(
            @RequestParam(name = "text", required = true) String text) {

        return itemService.findAvaliableItemByText(text);
    }

    //POST /items/{itemId}/comment
    @PostMapping("{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody NewCommentDto newComment,
                                    @PathVariable("itemId") Long itemId,
                                    @RequestHeader(USER_ID_HEADER_NAME) long userId) {

        newComment.setItemId(itemId);
        newComment.setAuthorId(userId);

        return itemService.createComment(newComment);
    }
}
