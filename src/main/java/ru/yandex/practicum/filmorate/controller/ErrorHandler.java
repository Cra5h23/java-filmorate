package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exeption.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Класс ErrorHandler предназначен для отлова ошибок запросов и возвращать пользователю форматированный ответ
 *
 * @author Nikolay Radzivon
 */
@ControllerAdvice("ru.yandex.practicum.filmorate")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<?> handlerFilmLikeServiceException(final FilmLikeServiceException e) {
        log.warn("Ошибка работы с лайками:", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка работы с лайками", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerFilmServiceException(final FilmServiceException e) {
        log.warn("Ошибка работы с фильмами:", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка работы с фильмами", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("Ошибка валидации:", e);
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка ввода данных", e.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handlerUserFriendServiceException(final UserFriendServiceException e) {
        log.warn("Ошибка работы с друзьями:", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка работы с друзьями", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handlerUserServiceException(final UserServiceException e) {
        log.warn("Ошибка работы с пользователями:", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка работы с пользователями", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerThrowable(Throwable e) {
        log.warn("Внутренняя ошибка сервера:", e);
        return ResponseEntity
                .internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Произошла внутренняя ошибка сервера", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerGenreServiceException(GenreServiceException e) {
        log.warn("Ошибка работы с жанрами:", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка работы с жанрами", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerRatingServiceException(RatingServiceException e) {
        log.warn("Ошибка работы с рейтингами:", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка работы с рейтингами", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerDirectorDaoException(DirectorServiceException e) {
        log.warn("Ошибка работы с режиссёрами:", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка работы с режиссёрами", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerBadRequestException(BadRequestException e) {
        log.warn("Ошибка 400:", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "error", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerNotFoundException(NotFoundException e) {
        log.warn("Ошибка 404:", e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "error", e.getMessage()));
    }
}
