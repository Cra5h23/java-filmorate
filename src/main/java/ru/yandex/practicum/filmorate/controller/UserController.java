package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserFriendService;
import ru.yandex.practicum.filmorate.service.user.UserService;

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

    @Qualifier("userFriendServiceDbImpl")
    private final UserFriendService userFriendService;

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("/GET getAllUsers");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user) {
        log.info("/POST addNewUser {}", user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.addUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("PUT updateUser {}", user);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.updateUser(user));
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<?> addUserToFriends(
            @PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("PUT addUserToFriends {} друга {}", userId, friendId);
        userFriendService.addingUserAsFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(required = false) Integer userId) {
        log.info("GET getUserById {}", userId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<?> deleteFromUserFriends(
            @PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("DELETE deleteFromUserFriends user {} friend {}", userId, friendId);
        return ResponseEntity.ok(userFriendService.deletingFromUserFriends(userId, friendId));
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getUsersFriends(@PathVariable Integer userId) {
        log.info("GET getUsersFriends for user id {}", userId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFriendService.getUserFriends(userId));
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<?> getUsersCommonFriends(
            @PathVariable Integer userId, @PathVariable Integer otherId) {
        log.info("GET getUsersCommonFriends for user {} otherUser {}", userId, otherId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFriendService.getListOfCommonFriends(userId, otherId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(required = false) int userId) {
        log.info("DELETE deleteUser {}", userId);
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }

    @PutMapping("/{userId}/confirmation/{friendId}/{confirm}")
    public ResponseEntity<?> confirmUserFriend(
            @PathVariable Integer userId, @PathVariable Integer friendId, @PathVariable boolean confirm) {
        return ResponseEntity.ok(userFriendService.confirmFriend(userId, friendId, confirm));
    }
}
