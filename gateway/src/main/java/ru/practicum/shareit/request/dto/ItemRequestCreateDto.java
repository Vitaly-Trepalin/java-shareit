package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemRequestCreateDto {
    private Long requestor;
    @Size(max = 320, message = "Описание вещи не может быть больше 320 символов")
    @NotBlank(message = "Описание вещи не может быть пустым")
    private String description;
}