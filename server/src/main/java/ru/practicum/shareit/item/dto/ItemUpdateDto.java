package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateDto {
    private Long id;
    @Size(min = 1, max = 100, message = "Название вещи не может быть меньше 1 и больше 100 символов")
    private String name;
    @Size(min = 1, max = 320, message = "Описание вещи не может быть меньше 1 и больше 320 символов")
    private String description;
    private Boolean available;
    private Long owner;
    private Long requestId;
}