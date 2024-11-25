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
    private Long id;
    Set<Long> likes = new HashSet<>();
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    @PastOrPresent
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
