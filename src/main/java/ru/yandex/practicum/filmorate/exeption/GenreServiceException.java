package ru.yandex.practicum.filmorate.exeption;

public class GenreServiceException extends RuntimeException{
    public GenreServiceException(String message) {
        super(message);
    }
}
