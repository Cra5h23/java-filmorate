package ru.yandex.practicum.filmorate.exeption;

public class FilmLikeServiceException extends RuntimeException {
    public FilmLikeServiceException(String message) {
        super(message);
    }
}
