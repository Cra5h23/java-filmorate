package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserFriendService {
    String addingUserAsFriend(int userId, int friendId);

    String deletingFromUserFriends(int userId, int deletingFriendId);

    Collection<User> getUserFriends(int userId);

    Collection<User> getListOfCommonFriends(int userId, int otherUserId);
}
