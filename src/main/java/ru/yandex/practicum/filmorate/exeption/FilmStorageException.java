package ru.yandex.practicum.filmorate.exeption;

public class FilmStorageException extends RuntimeException{
    public FilmStorageException(String message) {
        super(message);
    }
}