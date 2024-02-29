package ru.yandex.practicum.filmorate.exeption;

public class UserServiceException extends RuntimeException {
    public UserServiceException(String message) {
        super(message);
    }
}
