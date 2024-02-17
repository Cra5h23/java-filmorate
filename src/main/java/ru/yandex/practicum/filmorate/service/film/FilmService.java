package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.FilmServiceException;
import ru.yandex.practicum.filmorate.exeption.FilmStorageException;
import ru.yandex.practicum.filmorate.exeption.UserStorageException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @author Nikolay Radzivon
 */
@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    /**
     * Метод добавления лайка фильму
     *
     * @param filmId
     * @param userId
     * @return
     */
    public String addLikeFilm(Integer filmId, Integer userId) { //todo
        var film = checkFilm(filmId, "добавить", "фильму");
        checkUser(userId, "добавить", "фильму");
        if (film.getLikes().contains(userId)) {
            throw new FilmServiceException(String.format("Пользователь с id:%d уже поставил лайк фильму с id:%d", userId, filmId));
        }
        film.addLike(userId);
        log.info("Ползователь с id:{} поставил лайк фильму с id:{}", userId, filmId);
        return String.format("Пользователь с id:%d поставил лайк фильму с id:%d", userId, filmId);

        //var filmById = filmStorage.getFilmById(filmId);
//        if (filmById == null) {
//            throw new FilmServiceException(String.format("Попытка добавить лайк фильму с несуществующим id:%d", filmId));
//        }
//        if (userStorage.getUserById(userId) == null) {
//            throw new FilmServiceException(String.format("Попытка добавить лайк фильму от несуществующего пользователя id:%d", userId));
//        }
//        if (filmById.getLikes().contains(userId)) {
//            throw new FilmServiceException(String.format("Пользователь с id:%d уже поставил лайк фильму с id:%d", userId, filmId));
//        }


        //filmById.addLike(userId);

    }

    /**
     * Метод удаления лайка из фильма
     *
     * @param filmId
     * @param userId
     * @return
     */
    public String deleteLikeFilm(Integer filmId, Integer userId) {
        var film = checkFilm(filmId, "удалить", "у фильма");
        checkUser(userId, "удалить", "у фильма");
        film.deleteLike(userId);
        return String.format("Пользователь с id:%d удалил лайк у фильма с id:%d", userId, filmId);

//        var filmById = filmStorage.getFilmById(filmId);
//
//        if (filmById == null) {
//            throw new FilmServiceException(String.format("Попытка удалить лайк у фильма с несуществующим id:%d", filmId));
//        }
//        if (userStorage.getUserById(userId) == null) {
//            throw new FilmServiceException(String.format("Попытка удалить лайк у фильма от несуществующего пользователя id:%d", userId));
//        }
//        filmById.deleteLike(userId);

    }

    /**
     * @param count
     * @return
     */
    public Collection<Film> getMostPopularFilm(Integer count) {
        log.info("Запрошен список из {} самых популярных фильмов", count);
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(f -> (f.getLikes().size()) * -1))
                .limit(count)
                .collect(Collectors.toList());
    }

    private Film checkFilm(Integer filmId, String... s) {
        try {
            return filmStorage.getFilmById(filmId);
        } catch (FilmStorageException e) {
            //throw new FilmServiceException(String.format("Попытка добавить лайк фильму с несуществующим id:%d", filmId));
            throw new FilmServiceException(String.format("Попытка %s лайк %s с несуществующим id:%d", s[0], s[1], filmId));
        }
    }

    private User checkUser(Integer userId, String... s) {
        try {
            return userStorage.getUserById(userId);
        } catch (UserStorageException e) {
            //throw new FilmServiceException(String.format("Попытка добавить лайк фильму от несуществующего пользователя id:%d", userId));
            throw new FilmServiceException(String.format("Попытка %s лайк %s от несуществующего пользователя c id:%d", s[0], s[1], userId));
        }
    }
}
