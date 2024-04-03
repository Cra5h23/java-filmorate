package ru.yandex.practicum.filmorate.exeption;

public class ReviewServiceException extends RuntimeException {
    public ReviewServiceException(String message) {
        super(message);
    }
}
