package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exeption.LikeException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

/**
 * Класс ErrorHandler предназначен для отлова ошибок запросов и возвращать пользователю форматированный ответ
 *
 * @author Nikolay Radzivon
 */
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ErrorResponse handlerLikeException(final LikeException e) {
        return new ErrorResponse("Неверно введены данные", e.getMessage());
    }
}
