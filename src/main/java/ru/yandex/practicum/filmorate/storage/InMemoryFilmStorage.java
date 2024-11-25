package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 1;

    @Override
    public Collection<Film> getFilms() {
        log.info("Пришел Get запрос /films");
        log.info("Отправлен Get ответ /films с телом: {}", films.values());
        return films.values();
    }

    @Override
    public Film postFilm(@Valid @RequestBody Film film) {
        log.info("Добавление нового фильма {}", film.getName());

        checkDate(film.getReleaseDate());

        film = film.toBuilder().id(getNextId()).build();
        films.put(film.getId(), film);
        log.info("Отправлен ответ Post /users с телом: {}", films.get(film.getId()));

        return film;
    }

    @Override
    public Film putFilm(@Valid @RequestBody Film film) {
        Long filmId = film.getId();
        log.info("Пришел Put запрос /films с телом: {} ", film);

        checkDate(film.getReleaseDate());

        if (filmId == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        }

        if (films.containsKey(filmId)) {
            films.put(filmId, film);
            log.info("Отправлен Put ответ /films  с телом {}", films.get(filmId));
            return film;

        } else {
            log.error("Фильма с id: {} нет в списке фильмов", filmId);
            throw new NotFoundException("Фильма с id: " + filmId + " нет в списке фильмов");
        }
    }

    @Override
    public Optional<Film> findFilm(Long filmId) {
        if (films.containsKey(filmId)) {
            return Optional.of(films.get(filmId));
        }
        return Optional.empty();
    }

    private void checkDate(LocalDate filmDate) {
        log.info("Проверка даты фильма");
        if (filmDate.isBefore(LocalDate.parse("1895-12-28"))) {
            log.error("Дата релиза фильма раньше 1895-12-28");
            throw new ValidationException("Дата релиза фильма раньше 1895-12-28");
        }
    }

    private long getNextId() {
        return currentId++;
    }
}
