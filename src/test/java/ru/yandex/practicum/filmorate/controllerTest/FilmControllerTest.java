package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    static FilmController filmController;

    Film film = Film.builder()
                .name("filmName")
                .description("filmDescription")
                .releaseDate(LocalDate.of(2000, 1, 26))
                .duration(120)
                .build();

    @BeforeEach
    public void start() {
        filmController = new FilmController();
    }

    @Test
    public void shouldAddValidFilm() {
        assertEquals(filmController.postFilm(film), film);
    }

    @Test
    public void shouldNotAddFilmWithDateBeforeFilmsBirthdayDate() {
        Film newFilm = film.toBuilder().releaseDate(LocalDate.parse("1885-12-28")).build();

        assertThrows(ValidationException.class, () -> filmController.postFilm(newFilm));
    }

    @Test
    void shouldUpdateValidFilm() {
        filmController.postFilm(film);

        Film newFilm = film.toBuilder().description("newFilmDescription").id(1L).build();

        assertEquals(filmController.putFilm(newFilm), newFilm);
    }

    @Test
    public void shouldNotUpdateInvalidIdFilm() {
        filmController.postFilm(film);

        Film newFilm = film.toBuilder().id(3L).build();

        assertThrows(ValidationException.class, () -> filmController.putFilm(newFilm));
    }

    @Test
    public void shouldNotUpdateNoIdFilm() {
        filmController.postFilm(film);

        Film newFilm = film.toBuilder().description("newFilmDescription").id(null).build();

        assertThrows(ValidationException.class, () -> filmController.putFilm(newFilm));
    }
}
