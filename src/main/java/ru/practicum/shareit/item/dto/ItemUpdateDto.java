package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemUpdateDto {
    private Long id;
    @Size(min = 1, message = "Имя должно иметь больше одного символа")
    private String name;
    @Size(min = 1, message = "Описание должно иметь больше одного символа")
    private String description;
    private Boolean available;
    private Long owner;
    private Long request;

    public ItemUpdateDto() {
    }

    public ItemUpdateDto(Long id, String name, String description, Boolean available, Long owner, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}