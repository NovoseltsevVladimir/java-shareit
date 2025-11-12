package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.model.ItemForRequests;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class ItemRequestDtoWithAnswers extends ItemRequestDto {
    private List<ItemForRequests> items;
}
