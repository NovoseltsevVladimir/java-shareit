package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findAll(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable long itemId) {
        return itemService.findById(itemId);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody NewItemRequest item,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@Valid @RequestBody UpdateItemRequest newItem,
                          @PathVariable("itemId") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId) {
        newItem.setId(itemId);
        return itemService.update(newItem, userId);
    }

    @DeleteMapping("/{itemId}")
    public ItemDto deleteItem(@PathVariable("id") Long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId) {

        return itemService.delete(itemId, userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findAvaliableItemByText(
            @RequestParam(name = "text", required = true) String text) {

        return itemService.findAvaliableItemByText(text);
    }

}
