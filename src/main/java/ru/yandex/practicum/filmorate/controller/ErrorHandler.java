package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exeption.LikeException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;

import java.util.Map;

/**
 * Класс ErrorHandler предназначен для отлова ошибок запросов и возвращать пользователю форматированный ответ
 *
 * @author Nikolay Radzivon
 */
@ControllerAdvice("ru.yandex.practicum.filmorate")
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<?> handlerLikeException(final LikeException e) {
        return new ResponseEntity<>(
                Map.of("Неверно введены данные", e.getMessage()),
                HttpStatus.NOT_FOUND //todo исправить ResponseEntity
        );
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerValidationException(final ValidationException e) {
        return new ResponseEntity<>(
                Map.of("Неверно введены данные", e.getMessage()),
                HttpStatus.NO_CONTENT //todo проверить код и справить
        );
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("Неверно введены данные", e.getFieldError().getDefaultMessage()));
    }
}
