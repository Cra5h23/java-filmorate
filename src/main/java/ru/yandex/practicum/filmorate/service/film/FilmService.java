package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.LikeException;
import ru.yandex.practicum.filmorate.model.Film;
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
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    /**
     * Метод добавления лайка фильму
     *
     * @param filmId
     * @param userId
     * @return
     */
    public String addLikeFilm(Integer filmId, Integer userId) { //todo
        var filmById = filmStorage.getFilmById(filmId);
        if (filmById == null) {
            throw new LikeException(String.format("Попытка добавить лайк фильму с несуществующим id:%d", filmId));
        }
        if (userStorage.getUserById(userId) == null) {
            throw new LikeException(String.format("Попытка добавить лайк фильму от несуществующего пользователя id:%d", userId));
        }
        if (filmById.getLikes().contains(userId)) {
            throw new LikeException(String.format("Пользователь с id:%d уже поставил лайк фильму с id%d",userId , filmId));
        }

        filmById.addLike(userId);
        log.info("Ползователь с id:{} поставил лайк фисльму с id:{}", userId, filmId);
        return String.format("Пользователь с id:%d поставил лайк фильму с id:%d", userId, filmId);
    }

    /**
     * Метод удаления лайка из фильма
     * @param filmId
     * @param userId
     * @return
     */
    public String deleteLikeFilm(Integer filmId, Integer userId) {
        var filmById = filmStorage.getFilmById(filmId);
        if (filmById == null) {
            throw new LikeException(String.format("Попытка удалить лайк у фильма с несуществующим id:%d", filmId));
        }
        if (userStorage.getUserById(userId) == null) {
            throw new LikeException(String.format("Попытка удалить лайк у фильма от несуществующего пользователя id:%d", userId));
        }
        filmById.deleteLike(userId);
        return String.format("Пользователь с id:%d удалил лайк у фильма с id:%d", userId, filmId);
    }

    /**
     *
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
}
