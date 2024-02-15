package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author Nikolay Radzivon
 */
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Метод добавления пользователя в друзья
     * @param userId
     * @param friendId
     */
    public void addingUserAsFriend(int userId, int friendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new ValidationException(format("Нет пользователя с id:%d", userId)); // todo заменить ошибку на отдельную в этом сервисе
        }
        if (userStorage.getUserById(friendId) == null) {
            throw new ValidationException(format("Попытка добавить в друзья несуществующего пользователя с id:%d", friendId));
        }
        userStorage.getUserById(userId).addFriend(friendId);
        userStorage.getUserById(friendId).addFriend(userId);
    }

    public void deletingFromUserFriends(int userId, int deletingFriendId) {
        if (userStorage.getUserById(userId) == null) {
            throw new ValidationException(format("Нет пользователя с id:%d", userId));
        }
        if (userStorage.getUserById(deletingFriendId) == null) {
            throw new ValidationException(format("Попытка удалить из друзей несуществующего пользователя с id:%d", deletingFriendId));
        }
        userStorage.getUserById(userId).getFriends().remove(deletingFriendId);
    }

    public Collection<User> getAllUserFriends(int userId) {
        if (userStorage.getUserById(userId) == null) {
            throw new ValidationException(format("Нет пользователя с id:%d", userId));
        }
        return userStorage.getUserById(userId).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getListOfCommonFriends(int userId, int otherUserId) {
        if (userStorage.getUserById(userId) == null) {
            throw new ValidationException(format("Нет пользователя с id:%d", userId));
        }
        if (userStorage.getUserById(otherUserId) == null) {
            throw new ValidationException(format("Пользователь с которым вы хотите сравнить друзей не существует id:%d", otherUserId));
        }
        /*User userById = userStorage.getUserById(userId);
        User userById1 = userStorage.getUserById(otherUserId);
        List<Integer> collect = userById.getFriends().stream().filter(id -> userById1.getFriends().contains(id)).collect(Collectors.toList());
        return collect.stream().map(userStorage::getUserById).collect(Collectors.toList());*/

       return userStorage.getUserById(userId).getFriends().stream().filter(id-> userStorage.getUserById(otherUserId).getFriends().contains(id))
                .map(userStorage::getUserById).collect(Collectors.toList());
    }

    // Методы которые должны быть
    // добавление в друзья
    // удаление из друзей
    // вывод списка общих друзей.
}
