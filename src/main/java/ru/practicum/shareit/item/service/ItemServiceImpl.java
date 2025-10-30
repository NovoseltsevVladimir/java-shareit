package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotAvaliableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.comments.dto.CommentDto;
import ru.practicum.shareit.item.comments.dto.NewCommentDto;
import ru.practicum.shareit.item.comments.mapper.CommentMapper;
import ru.practicum.shareit.item.comments.model.Comment;
import ru.practicum.shareit.item.comments.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository repository;
    private UserRepository userRepository;
    private CommentRepository commentRepository;
    private BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository, UserRepository userRepository,
                           CommentRepository commentRepository, BookingRepository bookingRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Collection<ItemDto> findAll(Long userId) {

        User currentUser = userRepository.findById(userId).get();

        BooleanExpression byOwnerId = QItem.item.owner.id.eq(userId);

        Collection<ItemDto> itemDtoCollection = new ArrayList<>();
        for (Item item : repository.findAll(byOwnerId)) {
            itemDtoCollection.add(mapToItemToDto(item, currentUser));
        }

        return itemDtoCollection;
    }

    @Override
    public ItemDto findById(Long itemId, Long userId) {
        User currentUser = userRepository.findById(userId).get();
        Item item = repository.findById(itemId).get();
        if (item == null) {
            throw new NotFoundException("Вещь не найдена");
        }

        return mapToItemToDto(item, currentUser);
    }

    @Override
    public Item findAvaliableItemById(Long itemId) {

        Item item = repository.findById(itemId).get();
        if (item == null) {
            String bugText = "Вещь не найдена, id " + itemId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        if (item.isAvaliable() == false) {
            throw new NotAvaliableException("Вещь недоступна для бронирования");
        }

        return item;
    }

    @Override
    public ItemDto create(NewItemDto item, Long userId) {

        checkUser(userId);
        User currentUser = userRepository.findById(userId).get();

        Item newItem = ItemMapper.mapToItem(item);
        newItem.setOwner(userRepository.findById(userId).get());
        newItem = repository.save(newItem);

        return mapToItemToDto(newItem, currentUser);
    }

    @Override
    public ItemDto update(UpdateItemDto item, Long userId) {
        Long id = item.getId();

        checkUser(userId);
        checkUserPermissions(id, userId);

        User currentUser = userRepository.findById(userId).get();

        Item itemInMemory = repository.findById(id).get();
        if (itemInMemory == null) {
            String bugText = "Вещь не найдена, id " + id;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        Item updatedItem = repository.save(ItemMapper.updateItemFields(itemInMemory, item));
        updatedItem.setOwner(userRepository.findById(userId).get());

        return mapToItemToDto(updatedItem, currentUser);
    }

    @Override
    public ItemDto delete(Long itemId, Long userId) {

        checkUser(userId);
        checkUserPermissions(itemId, userId);

        User currentUser = userRepository.findById(userId).get();
        Item itemInMemory = repository.findById(itemId).get();

        if (itemInMemory == null) {
            String bugText = "Вещь не найдена, id " + itemId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        repository.delete(itemInMemory);

        return mapToItemToDto(itemInMemory, currentUser);
    }

    @Override
    public Collection<ItemDto> findAvaliableItemByText(String text, Long userId) {
        User currentUser = userRepository.findById(userId).get();
        if (text == null || text.isBlank() || text.isEmpty()) {
            return new ArrayList<>();
        }

        Collection<ItemDto> itemsDto = repository.search(text)
                .stream()
                .map(item -> mapToItemToDto(item, currentUser))
                .collect(Collectors.toList());

        return itemsDto;
    }

    private void checkUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            String bugText = "Пользователь не найден, id " + userId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }
    }

    private void checkUserPermissions(Long itemId, Long userId) {
        Item item = repository.findById(itemId).get();

        if (item == null) {
            String bugText = "Вещь не найдена, id " + itemId;
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        if (!item.getOwner().getId().equals(userId)) {
            String bugText = "Доступ запрещен, id пользователя " + userId + " id вещи " + itemId;
            log.warn(bugText);
            throw new ForbiddenException(bugText);
        }
    }

    @Override
    public CommentDto createComment(NewCommentDto newComment) {

        User user = userRepository.findById(newComment.getAuthorId()).get();
        if (user == null) {
            String bugText = "Пользователь не найден, id " + newComment.getAuthorId();
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        Item item = repository.findById(newComment.getItemId()).get();
        if (item == null) {
            String bugText = "Вещь не найдена, id " + newComment.getItemId();
            log.warn(bugText);
            throw new NotFoundException(bugText);
        }

        if (!didUserBookItem(user, item)) {
            String bugText = "Пользователь никогда не бронировал вещь id " + newComment.getItemId();
            log.warn(bugText);
            throw new NotAvaliableException(bugText);
        }

        newComment.setAuthor(user);
        newComment.setItem(item);

        Comment comment = commentRepository.save(CommentMapper.mapToComment(newComment));

        return CommentMapper.mapToCommentDto(comment);
    }

    private boolean didUserBookItem(User user, Item item) {
        List<Booking> bookings = bookingRepository.getItemBookings(item, Status.APPROVED, user, LocalDateTime.now());

        return bookings.size() > 0;
    }

    private ItemDto mapToItemToDto (Item item, User user) {
        ItemDto dto = ItemMapper.mapToItemDto(item);

        if (dto.getOwner()==user) {
            dto.setNextBooking(bookingRepository.findNextBookingDate(item, LocalDateTime.now(), Status.APPROVED));
            dto.setLastBooking(bookingRepository.findPastBookingDate(item, LocalDateTime.now(), Status.APPROVED));
        }

        return dto;
    }
}
