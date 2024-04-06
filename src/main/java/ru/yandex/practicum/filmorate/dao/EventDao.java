package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventDao {
    List<Event> getUserFeed(int userId);

    void createAddFilmLikeEvent(Integer filmId, Integer userId);

    void createDeleteFilmLikeEvent(Integer filmId, Integer userId);

    void createAddReviewEvent(Integer filmId, Integer userId);

    void createUpdateReviewEvent(Integer filmId, Integer userId);

    void createDeleteReviewEvent(Integer filmId, Integer userId);

    void createAddUserFriendEvent(Integer userId, Integer friendId);

    void createDeleteUserFriendEvent(Integer userId, Integer deletingFriendId);
}




