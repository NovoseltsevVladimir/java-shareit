
package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import static ru.practicum.shareit.ShareitConstants.USER_ID_HEADER_NAME;

@RestController
@RequestMapping("/items")
@Validated
public class ItemController {

    private ItemClient itemClient;

    @Autowired
    public ItemController(ItemClient itemClient) {
        this.itemClient = itemClient;
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(USER_ID_HEADER_NAME) Long userId) {
        return itemClient.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findById(@PathVariable Long itemId,
                                           @RequestHeader(USER_ID_HEADER_NAME) Long userId) {
        return itemClient.findById(itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody NewItemDto item,
                                         @RequestHeader(USER_ID_HEADER_NAME) Long userId) {
        return itemClient.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateItemDto newItem,
                                         @PathVariable("itemId") Long itemId,
                                         @RequestHeader(USER_ID_HEADER_NAME) Long userId) {
        newItem.setId(itemId);
        return itemClient.update(newItem, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@PathVariable("id") Long itemId,
                                             @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return itemClient.delete(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findAvaliableItemByText(
            @RequestParam(name = "text", required = true) String text,
            @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        return itemClient.findAvaliableItemByText(text, userId);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody NewCommentDto newComment,
                                                @PathVariable("itemId") Long itemId,
                                                @RequestHeader(USER_ID_HEADER_NAME) Long userId) {

        newComment.setItemId(itemId);
        newComment.setAuthorId(userId);

        return itemClient.createComment(newComment, itemId, userId);
    }

}

