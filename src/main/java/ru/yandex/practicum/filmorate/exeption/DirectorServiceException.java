package ru.yandex.practicum.filmorate.exeption;

public class DirectorServiceException extends RuntimeException {
    public DirectorServiceException(String message) {
        super(message);
    }
}
