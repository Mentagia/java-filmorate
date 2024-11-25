package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Value;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    @NotNull
    @PastOrPresent
    LocalDate birthday;
    Set<Long> friendsId = new HashSet<>();
}