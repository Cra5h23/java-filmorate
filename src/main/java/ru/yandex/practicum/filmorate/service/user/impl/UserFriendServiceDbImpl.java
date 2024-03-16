package ru.yandex.practicum.filmorate.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserFriendService;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
//@Primary
@Service
public class UserFriendServiceDbImpl implements UserFriendService {
    private final FriendDao friendDao;
    @Override
    public String addingUserAsFriend(Integer userId, Integer friendId) {
        friendDao.addingUserAsFriend(userId,friendId);
        return String.format("Пользователь с id: %d добавил в друзья пользователя с id: %d", userId, friendId);
    }

    @Override
    public String deletingFromUserFriends(Integer userId, Integer deletingFriendId) {
        friendDao.deletingFromUserFriends(userId, deletingFriendId);
        return String.format("Пользователь с id: %d удалил из друзей пользователя с id: %d", userId, deletingFriendId);
    }

    @Override
    public Collection<?> getUserFriends(Integer userId) {
        return friendDao.getUserFriends(userId);
    }

    @Override
    public Collection<User> getListOfCommonFriends(Integer userId, Integer otherUserId) {
        return friendDao.getListOfCommonFriends(userId, otherUserId);
    }

}
