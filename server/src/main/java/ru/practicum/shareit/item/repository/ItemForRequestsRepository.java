package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.ItemForRequests;

import java.util.List;

public interface ItemForRequestsRepository extends JpaRepository<ItemForRequests, Long> {

    @Query("select i from ItemForRequests i " +
            "where i.requestId IN (?1)")
    List<ItemForRequests> findByRequests(List<Long> idListOfRequests);

    @Query("select i from ItemForRequests i " +
            "where i.requestId IN (?1) and i.ownerId = ?2")
    List<ItemForRequests> findByRequestsAndOwner(List<Long> idListOfRequests, Long ownerId);
}
