package ru.practicum.shareit.request.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.ItemForRequests;
import ru.practicum.shareit.item.repository.ItemForRequestsRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithAnswers;
import ru.practicum.shareit.request.dto.NewItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestService {
    private ItemRequestRepository repository;
    private ItemForRequestsRepository repositoryForRequests;
    private UserService userService;

    @Autowired
    public ItemRequestService(ItemRequestRepository repository, ItemForRequestsRepository repositoryForRequests, UserService userService) {
        this.repository = repository;
        this.repositoryForRequests = repositoryForRequests;
        this.userService = userService;
    }

    public ItemRequestDto create(NewItemRequestDto newItemRequest, Long userId) {
        User user = userService.findUserById(userId);
        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(newItemRequest, user);
        itemRequest = repository.save(itemRequest);

        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    public List<ItemRequestDtoWithAnswers> getAllWithAnswers(Long userId) {
        User user = userService.findUserById(userId);

        List<ItemRequest> requests = repository.findByRequestor(user);

        List<Long> requestsId = requests
                .stream()
                .map(item -> item.getId())
                .collect(Collectors.toList());

        List<ItemForRequests> answers = repositoryForRequests.findByRequests(requestsId);

        List<ItemRequestDtoWithAnswers> result = new ArrayList<>();
        for (ItemRequest itemRequest : requests) {
            List<ItemForRequests> currentAnswers = answers
                    .stream()
                    .filter(itemForRequests ->
                            itemForRequests.getRequestId().equals(itemRequest.getId()))
                    .collect(Collectors.toList());

            result.add(ItemRequestMapper.mapToItemRequestDtoWithAnswers(itemRequest, currentAnswers));
        }

        return result;
    }

    public List<ItemRequestDto> getAllWithoutCurrentUser(Long userId) {
        User user = userService.findUserById(userId);

        return repository.findByRequestorIsNot(user)
                .stream()
                .map(itemRequest -> ItemRequestMapper.mapToItemRequestDto(itemRequest))
                .collect(Collectors.toList());
    }

    public ItemRequestDtoWithAnswers getById(Long requestId) {
        ItemRequest request = repository.findById(requestId).get();
        if (request == null) {
            String bugText = "Запрос не найден. id " + requestId;
            throw new NotFoundException(bugText);
        }

        List<Long> requestsId = new ArrayList<>();
        requestsId.add(requestId);

        List<ItemForRequests> answers = repositoryForRequests.findByRequests(requestsId);

        return ItemRequestMapper.mapToItemRequestDtoWithAnswers(request, answers);
    }


}
