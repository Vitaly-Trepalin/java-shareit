package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemCreateDto {
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Статус не может быть пустым")
    private Boolean available;
    private Long owner;
    private Long request;

    public ItemCreateDto() {
    }

    public ItemCreateDto(String name, String description, Boolean available, Long owner, Long request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
        this.request = request;
    }
}