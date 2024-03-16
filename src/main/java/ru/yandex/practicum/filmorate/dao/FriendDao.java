package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendDao {
    void addingUserAsFriend(Integer userId, Integer friendId);

    void deletingFromUserFriends(Integer userId, Integer deletingFriendId);

    Collection<?> getUserFriends(Integer userId);

    Collection<User> getListOfCommonFriends(Integer userId, Integer otherUserId);
}
