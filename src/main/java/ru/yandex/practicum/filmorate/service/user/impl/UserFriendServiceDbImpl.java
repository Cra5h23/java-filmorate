package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.exeption.UserFriendServiceException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserFriendService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

import static java.lang.String.format;

@RequiredArgsConstructor
@Slf4j
//@Primary
@Service
public class UserFriendServiceDbImpl implements UserFriendService {
    private final FriendDao friendDao;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    @Override
    public String addingUserAsFriend(Integer userId, Integer friendId) {
        checkId(userId, friendId, "добавить в друзья самого себя");
        checkUser(userId, "добавить друга для");
        checkUser(friendId, "добавить в друзья");
        friendDao.addingUserAsFriend(userId,friendId);
        return String.format("Пользователь с id: %d добавил в друзья пользователя с id: %d", userId, friendId);
    }

    @Override
    public String deletingFromUserFriends(Integer userId, Integer deletingFriendId) {
        checkId(userId, deletingFriendId, "удалить из друзей самого себя");
        checkUser(userId, "удалить друга для");
        checkUser(deletingFriendId, "удалить из друзей");
        friendDao.deletingFromUserFriends(userId, deletingFriendId);
        return String.format("Пользователь с id: %d удалил из друзей пользователя с id: %d", userId, deletingFriendId);
    }

    @Override
    public Collection<User> getUserFriends(Integer userId) {
        checkUser(userId, "получить список друзей для");
        return friendDao.getUserFriends(userId);
    }

    @Override
    public Collection<User> getListOfCommonFriends(Integer userId, Integer otherUserId) {
        checkId(userId, otherUserId, "получить список общих друзей самим с собой");
        checkUser(userId, "получить список общих друзей для");
        checkUser(otherUserId, "получить список общих друзей");
        return friendDao.getListOfCommonFriends(userId, otherUserId);
    }

    private void checkUser(Integer friendId, String... s) {
        userStorage.getUserById(friendId)
                .orElseThrow(() -> new UserFriendServiceException(
                        format("Попытка %s несуществующего пользователя с id: %d", s[0], friendId)));
    }
    private void checkId(Integer userId, Integer friendId, String s) {
        if (userId.equals(friendId)) {
            throw new UserFriendServiceException(String.format("Нельзя %s", s));
        }
    }
}
