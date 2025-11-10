package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class NewBookingDto {
    @FutureOrPresent
    private LocalDateTime start;
    @Future
    private LocalDateTime end;
    @NotNull
    private Long itemId;
    @NotNull
    private Long bookerId;

}
