package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    private Long id;

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
