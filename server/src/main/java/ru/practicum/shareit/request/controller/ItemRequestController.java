package ru.practicum.shareit.request.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.ShareitConstants.USER_ID_HEADER_NAME;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private ItemRequestService service;

    @Autowired
    public ItemRequestController(ItemRequestService service) {
        this.service = service;
    }

    @PostMapping
    public ItemRequestDto create(@RequestBody NewItemRequestDto itemRequest,
                                 @RequestHeader(USER_ID_HEADER_NAME) long userId) {
        return service.create(itemRequest, userId);
    }

    @GetMapping
    public List<ItemRequestDtoWithAnswers> getAllWithAnswers(@RequestHeader(USER_ID_HEADER_NAME) long userId) {
        return service.getAllWithAnswers(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllWithoutCurrentUser(@RequestHeader(USER_ID_HEADER_NAME) long userId) {
        return service.getAllWithoutCurrentUser(userId);
    }

    //GET /requests/{requestId}
    @GetMapping("/{requestId}")
    public ItemRequestDtoWithAnswers getItemRequest(@PathVariable(name = "requestId") Long requestId) {
        return service.getById(requestId);
    }
}
