package ru.yandex.practicum.filmorate.exeption;

public class FilmServiceException extends RuntimeException {
    public FilmServiceException(String message) {
        super(message);
    }
}
