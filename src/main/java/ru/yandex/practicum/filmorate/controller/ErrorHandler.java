package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exeption.FilmServiceException;
import ru.yandex.practicum.filmorate.exeption.FilmStorageException;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.exeption.UserStorageException;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Класс ErrorHandler предназначен для отлова ошибок запросов и возвращать пользователю форматированный ответ
 *
 * @author Nikolay Radzivon
 */
@ControllerAdvice("ru.yandex.practicum.filmorate")
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<?> handlerFilmServiceException(final FilmServiceException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString()
                        , "Ошибка установки лайка", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString()
                        , "Ошибка ввода данных", e.getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handlerUserServiceException(final UserServiceException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString(),
                        "Ошибка добавления пользователя в друзья", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerThrowable(Throwable e) {
        return ResponseEntity
                .internalServerError()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString()
                        , "Произошла внутренняя ошибка сервера", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerFilmStorageException(final FilmStorageException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString()
                        , "Ошибка получения фильма", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerUserStorageException(final UserStorageException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("timestamp", LocalDateTime.now().toString()
                        , "Ошибка получения пользователя", e.getMessage()));
    }
}
