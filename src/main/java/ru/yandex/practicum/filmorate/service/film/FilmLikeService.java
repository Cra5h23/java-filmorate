package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmLikeService {
    String addLikeFilm(Integer filmId, Integer userId);

    String deleteLikeFilm(Integer filmId, Integer userId);

    Collection<Film> getCommonFilms(Integer userId, Integer friendId);

    Collection<Film> getMostPopularFilm(Integer count, Integer genreId, Integer year);
}
