package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.FilmSort;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.exeption.FilmLikeServiceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmLikeService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmLikeServiceDbImpl implements FilmLikeService {
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    private final LikeDao likeDao;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Override
    public String addLikeFilm(Integer filmId, Integer userId) {
        checkFilm(filmId, "добавить", "фильму");
        checkUser(userId, "добавить", "фильму");
        likeDao.saveLike(filmId, userId);
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, filmId);
        return String.format("Пользователь с id: %d поставил лайк фильму с id: %d", userId, filmId);
    }

    @Override
    public String deleteLikeFilm(Integer filmId, Integer userId) {
        checkFilm(filmId, "удалить", "у фильма");
        checkUser(userId, "удалить", "у фильма");
        likeDao.deleteLike(filmId, userId);
        log.info("Пользователь с id: {} удалил лайк у фильма с id: {}", userId, filmId);
        return String.format("Пользователь с id: %d удалил лайк у фильма с id: %d", userId, filmId);
    }

    @Override
    public Collection<Film> getMostPopularFilm(Integer count) {
        log.info("Запрошена коллекция самых популярных фильмов в колличестве {} фильмов", count);
        return filmStorage.getSortedFilms(FilmSort.POPULAR_FILMS_DESC, count);
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        log.info("Запрошена коллекция общих с другом фильмов с сортировкой " +
                "по их популярности для пользователя {} и его друга {}", userId, friendId);
        checkUser(userId, "получить", "фильма");
        checkUser(friendId, "получить", "фильма");
        return filmStorage.getCommonFilms(userId, friendId);
    }

    private void checkFilm(Integer filmId, String... s) {
        filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmLikeServiceException(
                        String.format("Попытка %s лайк %s с несуществующим id: %d", s[0], s[1], filmId)));
    }

    private void checkUser(Integer userId, String... s) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new FilmLikeServiceException(
                        String.format("Попытка %s лайк %s от несуществующего пользователя c id: %d", s[0], s[1], userId)));
    }
}