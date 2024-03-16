package ru.yandex.practicum.filmorate.exeption;

public class RatingServiceException extends RuntimeException {
    public RatingServiceException(String message) {
        super(message);
    }
}
