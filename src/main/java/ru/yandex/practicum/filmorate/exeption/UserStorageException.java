package ru.yandex.practicum.filmorate.exeption;

public class UserStorageException extends RuntimeException {
    public UserStorageException(String message) {
        super(message);
    }
}
