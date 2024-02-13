package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addingUserAsFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new ValidationException(format("Нет пользователя с id:%d",userId));
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new ValidationException(format("Попытка добавить в друзья несуществующего пользователя с id:%d",friendId));
        }
        userStorage.getUserById(userId).getFriends().add(friendId);
    }

    }
}
