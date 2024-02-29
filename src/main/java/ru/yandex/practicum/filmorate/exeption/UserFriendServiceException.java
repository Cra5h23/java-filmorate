package ru.yandex.practicum.filmorate.exeption;

public class UserFriendServiceException extends RuntimeException {
    public UserFriendServiceException(String message) {
        super(message);
    }
}