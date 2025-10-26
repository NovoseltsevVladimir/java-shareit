package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotAvaliableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository repository;
    private UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<ItemDto> findAll(Long userId) {

        return repository.findAllUsersItems(userId)
                .stream()
                .map(item -> ItemMapper.mapToItemDto(item))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(Long itemId) {

        Item item = repository.findById(itemId);
        if (item == null) {
            throw new NotFoundException("Вещь не найдена");
        }

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public Item findAvaliableItemById(Long itemId) {

        Item item = repository.findById(itemId);
        if (item == null) {
            String bugText = "Вещь не найдена, id " + itemId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        item = repository.findAvaliableItemById(itemId);
       if (item == null) {
            throw new NotAvaliableException("Вещь недоступна для бронирования");
        }

        return item;
    }

    @Override
    public ItemDto create(NewItemDto item, Long userId) {

        checkUser(userId);

        Item newItem = ItemMapper.mapToItem(item);
        newItem.setOwner(userRepository.findById(userId));
        newItem = repository.create(newItem);

        return ItemMapper.mapToItemDto(newItem);
    }

    @Override
    public ItemDto update(UpdateItemDto item, Long userId) {
        Long id = item.getId();

        checkUser(userId);
        checkUserPermissions(id, userId);

        Item itemInMemory = repository.findById(id);
        if (itemInMemory == null) {
            String bugText = "Вещь не найдена, id " + id;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        Item updatedItem = repository.update(ItemMapper.updateItemFields(itemInMemory, item));
        updatedItem.setOwner(userRepository.findById(userId));

        return ItemMapper.mapToItemDto(updatedItem);
    }

    @Override
    public ItemDto delete(Long itemId, Long userId) {

        checkUser(userId);
        checkUserPermissions(itemId, userId);

        Item itemInMemory = repository.findById(itemId);

        if (itemInMemory == null) {
            String bugText = "Вещь не найдена, id " + itemId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        repository.delete(itemInMemory);

        return ItemMapper.mapToItemDto(itemInMemory);
    }

    @Override
    public Collection<ItemDto> findAvaliableItemByText(String text) {

        if (text == null || text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }

        Collection<ItemDto> itemsDto = repository.findAvaliableItemByText(text)
                .stream()
                .map(item -> ItemMapper.mapToItemDto(item))
                .collect(Collectors.toList());

        return itemsDto;
    }

    private void checkUser(Long userId) {
        if (!userRepository.isUserExist(userId)) {
            String bugText = "Пользователь не найден, id " + userId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }
    }

    private void checkUserPermissions(Long itemId, Long userId) {
        if (!repository.canUserEditItem(itemId, userId)) {
            String bugText = "Доступ запрещен, id пользователя " + userId + " id вещи " + itemId;
            log.warn(bugText);
            throw new ForbiddenException(bugText);
        }
    }
}
