package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import static ru.practicum.shareit.ShareitConstants.USER_ID_HEADER_NAME;

@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

    private ItemRequestClient itemRequestClient;

    @Autowired
    public ItemRequestController(ItemRequestClient itemRequestClient) {
        this.itemRequestClient = itemRequestClient;
    }

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody NewItemRequestDto itemRequest,
                                         @RequestHeader(USER_ID_HEADER_NAME) long userId) {
        return itemRequestClient.create(itemRequest, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllWithAnswers(@RequestHeader(USER_ID_HEADER_NAME) long userId) {
        return itemRequestClient.getAllWithAnswers(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllWithoutCurrentUser(@RequestHeader(USER_ID_HEADER_NAME) long userId) {
        return itemRequestClient.getAllWithoutCurrentUser(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@PathVariable(name = "requestId") Long requestId) {
        return itemRequestClient.getItemRequest(requestId);
    }
}
