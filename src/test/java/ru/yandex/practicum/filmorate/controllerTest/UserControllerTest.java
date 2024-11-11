package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    static UserController userController;

    User user = User.builder()
            .email("email@test.ru")
            .login("Login")
            .name("Name")
            .birthday(LocalDate.of(200, 1, 26))
            .build();

    @BeforeEach
    public void start() {
        userController = new UserController();
    }

    @Test
    public void shouldAddValidUser() {
        assertEquals(userController.postUser(user), user);
    }

    @Test
    public void shouldAddUserWithNameAsLoginIfNameWasBlank() {
        User newUser = user.toBuilder().name(null).build();

        assertNotEquals(newUser.getName(), userController.postUser(newUser).getName());
        assertEquals("Login", userController.postUser(newUser).getName());
    }

    @Test
    public void shouldNotAddUserWithSpaceInLogin() {
        User newUser = user.toBuilder().login("Log in").build();

        assertThrows(ValidationException.class, () -> userController.postUser(newUser));
    }

    @Test
    void shouldUpdateValidUser() {
        userController.postUser(user);

        User newUser = user.toBuilder().id(1L).login("Login234").build();

        assertEquals(userController.putUser(newUser), newUser);
    }

   @Test
    public void shouldNotUpdateInvalidIdUser() {
       userController.postUser(user);

       User newUser = user.toBuilder().id(3L).login("Login234").build();

       assertThrows(ValidationException.class, () -> userController.putUser(newUser));
    }

    @Test
    public void shouldNotUpdateNoIdFilm() {
        userController.postUser(user);

        User newUser = user.toBuilder().id(null).login("Login234").build();

        assertThrows(ValidationException.class, () -> userController.putUser(newUser));
    }
}
