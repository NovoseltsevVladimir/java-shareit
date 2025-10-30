package ru.practicum.shareit.item.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
public class ItemMapper {

    private static BookingRepository bookingRepository;

    @Autowired
    public ItemMapper(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public static Item mapToItem(NewItemDto request) {

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvaliable(request.getAvailable());
        item.setOwner(request.getOwner());
        item.setRequest(request.getRequest());

        return item;
    }

    public static ItemDto mapToItemDto(Item item, User user) {

        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvaliable());
        dto.setOwner(item.getOwner());
        dto.setRequest(item.getRequest());
        dto.setComments(item.getComments());
        if (user == item.getOwner()) {
            dto.setNextBooking(bookingRepository.findNextBookingDate(item, LocalDateTime.now(), Status.APPROVED));
            dto.setLastBooking(bookingRepository.findPastBookingDate(item, LocalDateTime.now(), Status.APPROVED));
        }
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
