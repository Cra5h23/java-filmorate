package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.UserServiceException;
import ru.yandex.practicum.filmorate.exeption.UserStorageException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * @author Nikolay Radzivon
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    /**
     * Метод добавления пользователя в друзья
     *
     * @param userId индификационный номер пользователя которому хотите добавить друга
     * @param friendId индификационный номер друга которого ходите добавить
     * @return строку с ответом
     */
    public String addingUserAsFriend(int userId, int friendId) {
        checkId(userId, friendId, "добавить в друзья самого себя");
        var user = checkUser(userId, "добавить друга для");
        var friend = checkUser(friendId, "добавить в друзья");
        user.addFriend(friendId);
        friend.addFriend(userId);
        return String.format("Пользователь с id: %d добавил в друзья пользователя с id: %d", userId, friendId);
    }

    public String deletingFromUserFriends(int userId, int deletingFriendId) {
        checkId(userId,deletingFriendId, "удалить из друзей самого себя");
        var user = checkUser(userId, "удалить друга для");
        var deletingFriend = checkUser(deletingFriendId, "удалить из друзей");
        user.deletingFriend(deletingFriendId);
        deletingFriend.deletingFriend(userId);
        return String.format("Пользователь с id: %d удалил из друзей пользователя с id: %d", userId, deletingFriendId);
    }

    public Collection<User> getUserFriends(int userId) {
        var user = checkUser(userId, "получить список друзей для");
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getListOfCommonFriends(int userId, int otherUserId) {
        checkId(userId, otherUserId, "получить список общих друзей самим с собой");
        var user = checkUser(userId, "получить список общих друзей для");
        var otherUser = checkUser(otherUserId, "получить список общих друзей");
        return user.getFriends().stream()
                .filter(id -> otherUser.getFriends().contains(id))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    private User checkUser(Integer friendId, String... s) {
        try {
            return userStorage.getUserById(friendId);
        } catch (UserStorageException e) {
            throw new UserServiceException(format("Попытка %s несуществующего пользователя с id: %d", s[0], friendId));
        }
    }

    private void checkId(Integer userId, Integer friendId, String s) {
        if (userId.equals(friendId)) {
            throw new UserServiceException(String.format("Нельзя %s", s));
        }
    }
}
