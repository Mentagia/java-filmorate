package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(long filmId, long userId) {

        log.info("Пришел PUT запрос /films/{}/like/{}: ", filmId, userId);

        Optional<Film> film = filmStorage.findFilm(filmId);
        Optional<User> user = userStorage.findUser(userId);


        if (film.isPresent() && user.isPresent()) {
            film.get().getLikes().add(userId);

            log.info("Отправлен PUT ответ /films/{}/like/{} с телом: {}", filmId, userId, film.get());
            return film.get();

        } else if (user.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");

        } else {
            log.error("Фильм с id = {} не найден", filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public Film removeLike(long filmId, long userId) {
        log.info("Пришел Delete запрос /films/{}/like/{}: ", filmId, userId);

        Optional<Film> film = filmStorage.findFilm(filmId);
        Optional<User> user = userStorage.findUser(userId);

        if (film.isPresent() && user.isPresent()) {
            film.get().getLikes().remove(userId);

            log.info("Отправлен Delete ответ /films/{}/like/{} с телом: {}", filmId, userId, film.get());
            return film.get();

        } else if (user.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");

        } else {
            log.error("Фильм с id = {} не найден", filmId);
            throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        }
    }

    public List<Film> getPopularFilms(Long count) {
        log.info("Пришел Get запрос /films/popular?count={}: ", count);
        Comparator<Film> filmComparator = Comparator.comparingInt(film -> film.getLikes().size());

        if (count <= 0) {
            log.error("Ошибка: значение count: {} меньше 0", count);
            throw new RuntimeException("Число отображаемых фильмов count не может быть меньше, либо равно 0");
        }

        List<Film> sortedFilms = filmStorage.getFilms()
                .stream()
                .sorted(filmComparator.reversed())
                .limit(count)
                .toList();

        log.info("Отправлен Get ответ /films/popular?count={}: с телом: {}", count, sortedFilms);
        return sortedFilms;

    }
}
