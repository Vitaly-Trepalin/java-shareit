package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.practicum.shareit.Marker;

@Data
public class UserDto {
    private Long id;
    @NotBlank(groups = Marker.OnCreate.class, message = "Имя не может быть пустым")
    private String name;
    @NotBlank(groups = Marker.OnCreate.class, message = "Email не может быть пустым")
    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message =
            "Email должен соответствовать своему формату")
    private String email;

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
