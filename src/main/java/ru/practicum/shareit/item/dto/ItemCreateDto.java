package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateDto {
    @NotBlank(message = "Название вещи не может быть пустым")
    @Size(max = 100, message = "Название вещи не может быть больше 100 символов")
    private String name;
    @NotBlank(message = "Описание вещи не может быть пустым")
    @Size(max = 320, message = "Описание вещи не может быть больше 320 символов")
    private String description;
    @NotNull(message = "Статус не может быть пустым")
    private Boolean available;
    private Long owner;
    private Long request;
}