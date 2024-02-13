package ru.yandex.practicum.filmorate.exeption;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }

    public ValidationException() {
    }

    public ValidationException(String message, Exception e) {
        super(message, e);
    }
}
