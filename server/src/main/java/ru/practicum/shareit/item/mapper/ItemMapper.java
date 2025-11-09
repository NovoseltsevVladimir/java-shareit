package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public static Item mapToItem(NewItemDto request) {

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvaliable(request.getAvailable());
        item.setOwner(request.getOwner());

        return item;
    }

    public static ItemDto mapToItemDto(Item item) {

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvaliable());
        dto.setOwner(item.getOwner());
        dto.setRequest(item.getRequest());
        dto.setComments(item.getComments());

        return dto;
    }

    public static Item updateItemFields(Item item, UpdateItemDto request) {

        if (request.hasName()) {
            item.setName(request.getName());
        }

        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }

        if (request.hasAvaliability()) {
            item.setAvaliable(request.getAvailable());
        }

        return item;
    }
}
