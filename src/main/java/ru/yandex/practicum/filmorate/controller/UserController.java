package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
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
        return Collections.unmodifiableCollection(userMap.values());
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
        log.info("Обновлена задача с id: {}", userId);
        return userMap.keySet().stream()
                .filter(id -> id.equals(userId))
                .map(userMap::get)
                .peek(u -> u.setName(user.getName()))
                .peek(u -> u.setLogin(user.getLogin()))
                .peek(u -> u.setEmail(user.getEmail()))
                .peek(u -> u.setBirthday(user.getBirthday()))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Нет пользователя с id: " + userId));
    }
}
