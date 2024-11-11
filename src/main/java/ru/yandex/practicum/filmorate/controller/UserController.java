package ru.yandex.practicum.filmorate.controller;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Запрос списка всех пользователей");
        return users.values();
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        log.info("Добавление нового пользователя");

        String userName = user.getName();
        String userLogin = user.getLogin();

        checkSpace(userLogin);

        if (userName == null || userName.isBlank()) {
            user.setName(userLogin);
        }

        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен", user.getName());

        return user;
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        Long userId = user.getId();
        String userName = user.getName();
        String userLogin = user.getLogin();

        if (userId == null) {
            log.error("Не указан id");
            throw new ValidationException("Не указан id");
        }

        checkSpace(userLogin);

        log.info("Обновление пользователя с id = {}",user.getId());
        if (userName == null || userName.isBlank()) {
            user.setName(userLogin);
        }

        if (users.containsKey(userId)) {
            users.put(userId, user);
            log.info("Пользователь с id:{} изменен", userId);
            return user;

        } else {
            log.error("Пользователя с id:{} нет в списке пользователей", userId);
            throw new ValidationException("Пользователя с id:" + userId + " нет в списке пользователей");
        }

    }

    private void checkSpace(String login) {
        if (login.contains(" ")) {
            log.error("Логин содержит пробел");
            throw new ValidationException("Логин содержит пробел");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
