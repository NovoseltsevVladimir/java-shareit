package ru.practicum.shareit.item.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "items", schema = "public")
@Getter
@Setter
public class ItemForRequests {
    @Id
    private Long id;
    @Column(name = "request_id")
    private Long requestId;
    @Column(name = "owner_id")
    private Long ownerId;
    @Column(name = "name")
    private String name;
}
