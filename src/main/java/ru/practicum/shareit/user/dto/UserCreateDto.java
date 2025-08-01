package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDto {
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @NotNull(message = "Email не может быть null")
    @Email(message = "Email должен соответствовать своему формату")
    private String email;

    public UserCreateDto() {
    }

    public UserCreateDto(Long id, String name, String email) {
        this.name = name;
        this.email = email;
    }
}
