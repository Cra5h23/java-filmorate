package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Like;
import java.util.List;

public interface LikeDao {

    void saveLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    List<Like> getAllFilmLikes();
}
