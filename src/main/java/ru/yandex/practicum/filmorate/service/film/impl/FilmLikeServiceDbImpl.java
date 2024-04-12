package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FilmSort;
import ru.yandex.practicum.filmorate.dao.event.EventDao;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.exeption.FilmLikeServiceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmLikeService;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmLikeServiceDbImpl implements FilmLikeService {
    private final FilmDao filmDao;
    private final LikeDao likeDao;
    private final UserDao userDao;
    private final EventDao eventDao;

    @Override
    public String addLikeFilm(Integer filmId, Integer userId) {
        checkFilm(filmId, "добавить", "фильму");
        checkUser(userId, "добавить", "фильму");
        likeDao.saveLike(filmId, userId);
        eventDao.createAddFilmLikeEvent(filmId,userId);
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, filmId);
        return String.format("Пользователь с id: %d поставил лайк фильму с id: %d", userId, filmId);
    }

    @Override
    public String deleteLikeFilm(Integer filmId, Integer userId) {
        checkFilm(filmId, "удалить", "у фильма");
        checkUser(userId, "удалить", "у фильма");
        likeDao.deleteLike(filmId, userId);
        eventDao.createDeleteFilmLikeEvent(filmId,userId);
        log.info("Пользователь с id: {} удалил лайк у фильма с id: {}", userId, filmId);
        return String.format("Пользователь с id: %d удалил лайк у фильма с id: %d", userId, filmId);
    }

    @Override
    public Collection<Film> getMostPopularFilm(Integer count, Integer genreId, Integer year) {
        log.info("Запрошена коллекция самых популярных фильмов в количестве {} фильмов", count);
        return filmDao.getSortedFilms(FilmSort.POPULAR_FILMS_DESC, count, genreId, year);
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        log.info("Запрошена коллекция общих с другом фильмов с сортировкой " +
                "по их популярности для пользователя {} и его друга {}", userId, friendId);
        checkUser(userId, "получить", "фильма");
        checkUser(friendId, "получить", "фильма");
        return filmDao.getSortedFilms(FilmSort.COMMON_FILMS_DESC,userId,friendId);
    }

    private void checkFilm(Integer filmId, String... s) {
        filmDao.getFilmById(filmId)
                .orElseThrow(() -> new FilmLikeServiceException(
                        String.format("Попытка %s лайк %s с несуществующим id: %d", s[0], s[1], filmId)));
    }

    private void checkUser(Integer userId, String... s) {
        userDao.getUserById(userId)
                .orElseThrow(() -> new FilmLikeServiceException(
                        String.format("Попытка %s лайк %s от несуществующего пользователя c id: %d", s[0], s[1], userId)));
    }
}