package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 100, message = "Имя не может быть больше 100 символов")
    private String name;
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен соответствовать своему формату")
    @Size(max = 100, message = "Email не может быть больше 320 символов")
    private String email;
}