package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserFriendService;

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
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserFriendService userFriendService;

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.addUser(user));
    }


    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.updateUser(user));
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<?> addUserToFriends(
            @PathVariable Integer userId, @PathVariable Integer friendId) {
        return ResponseEntity.ok(userFriendService.addingUserAsFriend(userId, friendId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(required = false) Integer userId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<?> deleteFromUserFriends(
            @PathVariable Integer userId, @PathVariable Integer friendId) {
        return ResponseEntity.ok(userFriendService.deletingFromUserFriends(userId, friendId));
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getUsersFriends(@PathVariable Integer userId) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFriendService.getUserFriends(userId));
    }

    @GetMapping("{userId}/friends/common/{otherId}")
    public ResponseEntity<?> getUsersCommonFriends(
            @PathVariable Integer userId, @PathVariable Integer otherId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFriendService.getListOfCommonFriends(userId, otherId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(required = false) int userId) {
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }
}
