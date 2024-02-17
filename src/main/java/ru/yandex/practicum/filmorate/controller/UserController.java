package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
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
@AllArgsConstructor
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
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
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> addUserToFriends(
            @PathVariable(name = "id") Integer userId, @PathVariable Integer friendId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.addingUserAsFriend(userId, friendId));
    }


}
