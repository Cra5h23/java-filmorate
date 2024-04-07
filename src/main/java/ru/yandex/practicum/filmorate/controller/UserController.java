package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
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

    private final FilmService filmService;

    private final UserFriendService userFriendService;

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUsers() {
        log.info("GET /users");
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user) {
        log.info("POST /users User {}", user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.addUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("PUT /users/ User {}", user);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.updateUser(user));
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<?> addUserToFriends(
            @PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("PUT /users/{}/friends/{}", userId, friendId);
        userFriendService.addingUserAsFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable(required = false) Integer userId) {
        log.info("GET /users/{}", userId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getUserById(userId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<?> deleteFromUserFriends(
            @PathVariable Integer userId, @PathVariable Integer friendId) {
        log.info("DELETE /users/{}/friends/{}", userId, friendId);
        return ResponseEntity.ok(userFriendService.deletingFromUserFriends(userId, friendId));
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<?> getUserFriends(@PathVariable Integer userId) {
        log.info("GET /users/{}", userId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFriendService.getUserFriends(userId));
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public ResponseEntity<?> getUsersCommonFriends(
            @PathVariable Integer userId, @PathVariable Integer otherId) {
        log.info("GET /users/{}/friends/common/{}", userId, otherId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userFriendService.getListOfCommonFriends(userId, otherId));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(required = false) int userId) {
        log.info("DELETE /users/{}", userId);
        return ResponseEntity.ok(userService.deleteUserById(userId));
    }

    @PutMapping("/{userId}/confirmation/{friendId}/{confirm}")
    public ResponseEntity<?> confirmUserFriend(
            @PathVariable Integer userId, @PathVariable Integer friendId, @PathVariable boolean confirm) {
        log.info("PUT /users/{}/confirmation/{}/{}", userId, friendId, confirm);
        return ResponseEntity.ok(userFriendService.confirmFriend(userId, friendId, confirm));
    }

    @GetMapping("{userId}/recommendations")
    public ResponseEntity<Collection<Film>> getRecommendationsByUserId(@PathVariable Integer userId) {
        log.info("GET /users/{}/recommendations", userId);
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filmService.getRecommendationsByUserId(userId));
    }
}
