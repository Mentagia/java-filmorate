package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getFilms();

    Film postFilm(Film film);

    Film putFilm(Film newFilm);

    Optional<Film> findFilm(Long filmId);
}
