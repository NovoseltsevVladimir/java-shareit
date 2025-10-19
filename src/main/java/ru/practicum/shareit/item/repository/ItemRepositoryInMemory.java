package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Repository
public class ItemRepositoryInMemory implements ItemRepository {

    private HashMap<Long, Item> items;
    private HashMap<Long, Item> avaliableItems;
    private HashMap<Long, Long> owners;

    private Long maxId;

    @Autowired
    public ItemRepositoryInMemory() {
        items = new HashMap<>();
        avaliableItems = new HashMap<>();
        owners = new HashMap<>();
        maxId = 0L;
    }

    public Long getNewId() {
        maxId++;
        return maxId;
    }

    @Override
    public Collection<Item> findAllUsersItems(Long userId) {
        Collection<Item> usersItems = new ArrayList<>();

        for (Long itemId : owners.keySet()) {
            Long currentUserId = owners.get(itemId);
            if (currentUserId.equals(userId)) {
                usersItems.add(items.get(itemId));
            }
        }

        return usersItems;
    }

    @Override
    public Item findById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> findAvaliableItemByText(String text) {

        Collection<Item> itemsCollection = new ArrayList<>();

        for (Item item : avaliableItems.values()) {
            String currentString = item.getName();
            if (currentString.toUpperCase().contains(text.toUpperCase())) {
                itemsCollection.add(item);
            }
        }

        return itemsCollection;
    }

    @Override
    public Item create(Item item) {

        Long ownerId = item.getOwner();
        Long itemId = getNewId();
        item.setId(itemId);

        items.put(itemId, item);
        if (item.isAvailable()) {
            avaliableItems.put(itemId, item);
        }

        owners.put(itemId, ownerId);

        return item;
    }

    @Override
    public Item update(Item item) {

        Long id = item.getId();

        Item avaliableItem = avaliableItems.get(id);
        if (avaliableItem == null && item.isAvailable()) {
            avaliableItems.put(id, item);
        } else if (avaliableItem != null && !item.isAvailable()) {
            avaliableItems.remove(id);
        }

        items.put(id, item);

        return item;
    }

    @Override
    public Item delete(Item item) {
        Long id = item.getId();
        items.remove(id);
        avaliableItems.remove(id);
        owners.remove(id);

        return item;
    }

    @Override
    public boolean canUserEditItem(Long itemId, Long userId) {
        return owners.get(itemId).equals(userId);
    }
}
