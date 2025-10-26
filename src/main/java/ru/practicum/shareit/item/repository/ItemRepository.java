package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Collection<Item> findAllUsersItems(Long userId);

    Item findById(Long itemId);

    Item findAvaliableItemById(Long itemId);

    Collection<Item> findAvaliableItemByText(String text);

    Item create(Item item);

    Item update(Item item);

    Item delete(Item item);

    boolean canUserEditItem(Long itemId, Long userId);
}
