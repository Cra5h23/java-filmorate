package ru.yandex.practicum.filmorate.service.film.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.FilmSort;
import ru.yandex.practicum.filmorate.exeption.FilmLikeServiceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmLikeService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

/**
 * Сервис добавления удаления лайков фильмам
 *
 * @author Nikolay Radzivon
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FilmLikeServiceImpl implements FilmLikeService {
    @Qualifier("inMemoryFilmStorage")
    private final FilmStorage filmStorage;
    @Qualifier("inMemoryUserStorage")
    private final UserStorage userStorage;

    /**
     * Метод добавления лайка фильму
     *
     * @param filmId Индификационный номер фильма
     * @param userId Индификационный номер пользователя
     * @return Возвращает строку с ответом
     */
    public String addLikeFilm(Integer filmId, Integer userId) {
        var film = checkFilm(filmId, "добавить", "фильму");
        checkUser(userId, "добавить", "фильму");
        if (film.getLikes().contains(userId)) {
            throw new FilmLikeServiceException(String.format(
                    "Пользователь с id: %d уже поставил лайк фильму с id: %d", userId, filmId));
        }
        film.getLikes().add(userId);
        log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, filmId);
        return String.format("Пользователь с id: %d поставил лайк фильму с id: %d", userId, filmId);
    }

    /**
     * Метод удаления лайка из фильма
     *
     * @param filmId Индификационный номер фильма
     * @param userId Индификационный номер пользователя
     * @return Возвращает строку с ответом
     */
    public String deleteLikeFilm(Integer filmId, Integer userId) {
        var film = checkFilm(filmId, "удалить", "у фильма");
        checkUser(userId, "удалить", "у фильма");
        film.getLikes().remove(userId);
        return String.format("Пользователь с id: %d удалил лайк у фильма с id: %d", userId, filmId);
    }

    /**
     * Метод получения списка самых популярных фильмов
     *
     * @param count колличество фильмов в списке самых популярных фильмов
     * @return список самых популярных фильмаов
     */
    public Collection<Film> getMostPopularFilm(Integer count) {
        log.info("Запрошен список из {} самых популярных фильмов", count);
        return filmStorage.getSortedFilms(FilmSort.POPULAR_FILMS_DESC, count);
    }

    private Film checkFilm(Integer filmId, String... s) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmLikeServiceException(
                        String.format("Попытка %s лайк %s с несуществующим id: %d", s[0], s[1], filmId)));
    }

    private User checkUser(Integer userId, String... s) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new FilmLikeServiceException(
                        String.format("Попытка %s лайк %s от несуществующего пользователя c id: %d", s[0], s[1], userId)));
    }
}