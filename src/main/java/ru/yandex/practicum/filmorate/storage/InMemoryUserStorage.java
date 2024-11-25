package ru.yandex.practicum.filmorate.storage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 1;

    @Override
    public Collection<User> getUsers() {
        log.info("Пришел Get запрос /users");
        log.info("Отправлен Get ответ / users с телом: {}", users.values());
        return users.values();
    }

    @Override
    public User postUser(@Valid @RequestBody User user) {
        log.info("Пришел Post запрос /users с телом: {} ", user);

        String userName = user.getName();
        String userLogin = user.getLogin();

        checkSpace(userLogin);

        if (userName == null || userName.isBlank()) {
           user = user.toBuilder().name(userLogin).build();
        }

        user = user.toBuilder().id(getNextId()).build();
        users.put(user.getId(), user);
        log.info("Отправлен ответ Post /users с телом: {}", users.get(user.getId()));

        return user;
    }

    @Override
    public User putUser(@Valid @RequestBody User user) {
        log.info("Пришел Put запрос /users с телом: {} ", user);

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
            user = user.toBuilder().name(userLogin).build();
        }

        if (users.containsKey(userId)) {
            users.put(userId, user);
            log.info("Отправлен Put ответ /films  с телом {}", users.get(userId));
            return user;

        } else {
            log.error("Пользователя с id:{} нет в списке пользователей", userId);
            throw new NotFoundException("Пользователя с id:" + userId + " нет в списке пользователей");
        }

    }

    @Override
    public Optional<User> findUser(Long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        } else
            return Optional.empty();
    }

    private void checkSpace(String login) {
        log.info("Проверка логина на содержание пробелов");
        if (login.contains(" ")) {
            log.error("Логин содержит пробел");
            throw new ValidationException("Логин содержит пробел");
        }
    }

    private long getNextId() {
        return currentId++;
    }
}
