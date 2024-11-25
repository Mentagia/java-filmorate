package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;


import java.time.LocalDate;

import java.util.HashSet;
import java.util.Set;

@Value
@Builder(toBuilder = true)
public class Film {
    Long id;
    Set<Long> likes = new HashSet<>();
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    @NotNull
    @PastOrPresent
    LocalDate releaseDate;
    @Positive
    int duration;
}
