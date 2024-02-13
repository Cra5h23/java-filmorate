package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс UserController
 *
 * @author Nikolay Radzivon
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Getter
    private final Map<Integer, User> userMap = new HashMap<>();

    private int generatorUserId = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userMap.values();
    }

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        user.setId(++generatorUserId);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userMap.put(user.getId(), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }


    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        int userId = user.getId();
        User u = userMap.get(userId);
        if (u == null) {
            log.warn("Нет пользователя с id: {}", userId);
            throw new ValidationException("Нет пользователя с id: " + userId);
        }
        u.setName(user.getName());
        u.setLogin(user.getLogin());
        u.setEmail(user.getEmail());
        u.setBirthday(user.getBirthday());
        log.info("Обновлён пользователь с id: {}", userId);
        return u;
    }
}
