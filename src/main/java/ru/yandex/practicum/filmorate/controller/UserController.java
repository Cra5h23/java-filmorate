package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Класс UserController
 *
 * @author Nikolay Radzivon
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
//    @Getter
//    private final Map<Integer, User> userMap = new HashMap<>();//todo удалить
//
//    private int generatorUserId = 0;//todo удалить

    private final UserStorage userStorage;

    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
//        return userMap.values();
    }

    @PostMapping
    public User addNewUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
//        user.setId(++generatorUserId);
//        if (user.getName() == null || user.getName().isBlank()) {
//            user.setName(user.getLogin());
//        }
//        userMap.put(user.getId(), user);
//        log.info("Добавлен пользователь {}", user);
//        return user;
    }


    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
//        if (user.getName() == null || user.getName().isBlank()) {
//            user.setName(user.getLogin());
//        }
//        int userId = user.getId();
//        User u = userMap.get(userId);
//        if (u == null) {
//            log.warn("Нет пользователя с id: {}", userId);
//            throw new ValidationException("Нет пользователя с id: " + userId);
//        }
//        u.setName(user.getName());
//        u.setLogin(user.getLogin());
//        u.setEmail(user.getEmail());
//        u.setBirthday(user.getBirthday());
//        log.info("Обновлён пользователь с id: {}", userId);
//        return u;
    }


}
