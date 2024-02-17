package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exeption.*;

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
                .body(Map.of("Неверно введены данные", e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("Неверно введены данные", e.getFieldError().getDefaultMessage()));
    }
}
