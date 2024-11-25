package ru.yandex.practicum.filmorate.service;

import com.sun.tools.jconsole.JConsoleContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public Collection<User> getFriends(long userId) {
        log.info("Пришел Get запрос /users/{}/friends", userId);
        Optional<User> currentUser = storage.findUser(userId);

        if (currentUser.isPresent()) {
            Set<Long> friends = currentUser.get().getFriendsId();

            List<User> friendsList = storage.getUsers()
                    .stream()
                    .filter(user -> friends.contains(user.getId()))
                    .toList();
            log.info("Отправлен Get ответ /users/{}/friends с телом: {}", userId, friendsList);
            return friendsList;

        } else {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
    }

    public Collection<User> getCommonFriends(long userId, long secondUserId) {

        log.info("Пришел Get запрос /users/{}/friends/common/{}", userId, secondUserId);

        Optional<User> firstUser = storage.findUser(userId);
        Optional<User> secondUser = storage.findUser(secondUserId);

        if (firstUser.isPresent() && secondUser.isPresent()) {
            Set<Long> firstFriends = firstUser.get().getFriendsId();
            Set<Long> secondFriends = secondUser.get().getFriendsId();

            List<Long> commonIds = firstFriends
                    .stream()
                    .filter(secondFriends::contains)
                    .toList();

            List<User> commonFriends = storage.getUsers()
                    .stream()
                    .filter(user -> commonIds.contains(user.getId()))
                    .toList();

            log.info("Отправлен Get ответ /users/{}/friends/common/{} с телом: {}", userId, secondUserId, commonFriends);
            return commonFriends;

        } else if (firstUser.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");

        } else {
            log.error("Пользователь с id = {} не найден", secondUserId);
            throw new NotFoundException("Пользователь с id = " + secondUserId + " не найден");
        }
    }


    public User addFriend(long userId, long friendUserId) {
        log.info("Пришел PUT запрос /users/{}/friends/{}: ", userId, friendUserId);

        if (userId == friendUserId) {
            log.error("Ошибка: Id пользователя {} равен id друга {}", userId, friendUserId);
            throw new ValidationException("Нельзя добавить в друзья самого себя");
        }

        Optional<User> user = storage.findUser(userId);
        Optional<User> friendUser = storage.findUser(friendUserId);

        if (user.isPresent() && friendUser.isPresent()) {
            user.get().getFriendsId().add(friendUserId);
            friendUser.get().getFriendsId().add(userId);

            log.info("Отправлен Put ответ /users/{}/friends/{} с телом: {}", userId, friendUserId, user);
            return user.get();

        } else if (user.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");

        } else {
            log.error("Пользователь с id = {} не найден", friendUserId);
            throw new NotFoundException("Пользователь с id = " + friendUserId + " не найден");
        }
    }

    public User removeFriend(long userId, long friendUserId) {
        log.info("Пришел Delete запрос /users/{}/friends/{}: ", userId, friendUserId);

        if (userId == friendUserId) {
            log.error("Ошибка: Id пользователя {} равен id друга {}", userId, friendUserId);
            throw new ValidationException("Id пользователя: " + userId  + " идентичен Id друга: "+ friendUserId);
        }

        Optional<User> user = storage.findUser(userId);
        Optional<User> friendUser = storage.findUser(friendUserId);

        if (user.isPresent() && friendUser.isPresent()) {
            user.get().getFriendsId().remove(friendUserId);
            friendUser.get().getFriendsId().remove(userId);

            log.info("Отправлен DELETE ответ /users/{}/friends/{} с телом: {}", userId, friendUserId, user);
            return user.get();

        } else if (user.isEmpty()) {
            log.error("Пользователь с id = {} не найден", userId);
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");

        } else {
            log.error("Пользователь с id = {} не найден", friendUserId);
            throw new NotFoundException("Пользователь с id = " + friendUserId + " не найден");
        }
    }
}
