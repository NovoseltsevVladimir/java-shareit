package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.ItemForRequests;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(NewItemRequestDto request, User requestor) {

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(request.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequest;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {

        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(itemRequest.getId());
        dto.setCreated(itemRequest.getCreated());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestor(itemRequest.getRequestor());

        return dto;
    }

    public static ItemRequestDtoWithAnswers mapToItemRequestDtoWithAnswers(ItemRequest itemRequest,
                                                                List<ItemForRequests> answers) {

        ItemRequestDtoWithAnswers dto = new ItemRequestDtoWithAnswers();
        dto.setId(itemRequest.getId());
        dto.setCreated(itemRequest.getCreated());
        dto.setDescription(itemRequest.getDescription());
        dto.setRequestor(itemRequest.getRequestor());
        dto.setItems(answers);

        return dto;
    }

}
