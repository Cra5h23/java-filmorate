package ru.yandex.practicum.filmorate.dao.event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventDao {
    List<Event> getUserFeed(int userId);

    void createAddFilmLikeEvent(Integer filmId, Integer userId);

    void createDeleteFilmLikeEvent(Integer filmId, Integer userId);

    void createAddReviewEvent(Integer reviewId, Integer userId);

    void createUpdateReviewEvent(Integer reviewId, Integer userId);

    void createDeleteReviewEvent(Integer reviewId, Integer userId);

    void createAddUserFriendEvent(Integer userId, Integer friendId);

    void createDeleteUserFriendEvent(Integer userId, Integer deletingFriendId);
}




