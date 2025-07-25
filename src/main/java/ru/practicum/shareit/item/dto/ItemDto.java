package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.Marker;

@Data
public class ItemDto {
    private Long id;
    @NotBlank(groups = Marker.OnCreate.class, message = "Имя не может быть пустым")
    private String name;
    @NotBlank(groups = Marker.OnCreate.class, message = "Описание не может быть пустым")
    private String description;
    @NotNull(groups = Marker.OnCreate.class, message = "Статус не может быть пустым")
    private Boolean available;
    private Long request;

    public ItemDto() {
    }

    public ItemDto(Long id, String name, String description, boolean available, Long request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }
}