package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 1;

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Запрос списка всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма {}", film.getName());

        checkDate(film.getReleaseDate());

        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен c id {}", film.getName(), film.getId());

        return film;
    }

    @PutMapping
    public Film putFilm(@Valid @RequestBody Film film) {
        Long filmId = film.getId();
        log.info("Обновление фильма с id = {}", filmId);

        checkDate(film.getReleaseDate());

        if (filmId == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        }

        if (films.containsKey(filmId)) {
            films.put(filmId, film);
            return film;

        } else {
            log.error("Фильма с id: {} нет в списке фильмов", filmId);
            throw new ValidationException("Фильма с id: " + filmId + " нет в списке фильмов");
        }
    }

    private void checkDate(LocalDate filmDate) {
        if (filmDate.isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("Дата релиза фильма раньше 1895-12-28");
            throw new ValidationException("Дата релиза фильма раньше 1895-12-28");
        }
    }

    private long getNextId() {
        return currentId++;
    }

    /*private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }*/

}
