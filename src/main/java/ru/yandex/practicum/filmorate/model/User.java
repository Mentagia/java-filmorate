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
    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;
    private Set<Long> friendsId = new HashSet<>();
}