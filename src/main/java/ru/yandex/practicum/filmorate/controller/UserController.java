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

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(required = false, name = "id") Integer userId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userStorage.getUserById(userId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<?> deleteFromUserFriends(
            @PathVariable(name = "id") Integer userId, @PathVariable Integer friendId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.deletingFromUserFriends(userId, friendId));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<?> getUsersFriends(@PathVariable(name = "id") Integer userId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUserFriends(userId));
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<?> getUsersCommonFriends(
            @PathVariable(name = "id") Integer userId, @PathVariable Integer otherId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getListOfCommonFriends(userId, otherId));
    }

}
