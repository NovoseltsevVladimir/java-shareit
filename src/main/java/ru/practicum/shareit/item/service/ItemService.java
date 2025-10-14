package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> findAll(Long userId);

    ItemDto findById(Long itemId);

    ItemDto create(NewItemRequest item, Long userId);

    ItemDto update(UpdateItemRequest itemId, Long userId);

    ItemDto delete(Long itemId, Long userId);

    Collection<ItemDto> findAvaliableItemByText(String text);
}
