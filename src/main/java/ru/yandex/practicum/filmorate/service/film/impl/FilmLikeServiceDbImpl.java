package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.FilmSort;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmLikeService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmLikeServiceDbImpl implements FilmLikeService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final LikeDao likeDao;

    @Override
    public String addLikeFilm(Integer filmId, Integer userId) {
        likeDao.saveLike(filmId, userId);
        return String.format("Пользователь с id: %d поставил лайк фильму с id: %d", userId, filmId);
    }

    @Override
    public String deleteLikeFilm(Integer filmId, Integer userId) {
        likeDao.deleteLike(filmId, userId);
        return String.format("Пользователь с id: %d удалил лайк у фильма с id: %d", userId, filmId);
    }

    @Override
    public Collection<Film> getMostPopularFilm(Integer count) {
        return filmStorage.getSortedFilms(FilmSort.POPULAR_FILMS_DESC, count);
    }
}