package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDto {
    private Long id;
    private String name;
    @Email(message = "Email должен соответствовать своему формату")
    private String email;

    public UserUpdateDto() {
    }

    public UserUpdateDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
