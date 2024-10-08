package ru.yandex.practicum.filmorate.dao.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendDao {
    void addingUserAsFriend(Integer userId, Integer friendId);

    void deletingFromUserFriends(Integer userId, Integer deletingFriendId);

    Collection<User> getUserFriends(Integer userId);

    Collection<User> getListOfCommonFriends(Integer userId, Integer otherUserId);

    String confirmFriend(Integer userId, Integer friendId, boolean confirm);
}
