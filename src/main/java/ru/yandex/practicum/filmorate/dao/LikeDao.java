package ru.yandex.practicum.filmorate.dao;

public interface LikeDao  {

     void saveLike(Integer filmId, Integer userId);
     void deleteLike(Integer filmId, Integer userId);
}
