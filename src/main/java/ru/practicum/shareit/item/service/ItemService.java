package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {

    Collection<ItemDto> findAll(Long userId);

    ItemDto findById(Long itemId);

    Item findAvaliableItemById(Long itemId);

    ItemDto create(NewItemDto item, Long userId);

    ItemDto update(UpdateItemDto itemId, Long userId);

    ItemDto delete(Long itemId, Long userId);

    Collection<ItemDto> findAvaliableItemByText(String text);
}
